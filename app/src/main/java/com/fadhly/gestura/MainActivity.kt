package com.fadhly.gestura

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var recordButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var responseTextView: TextView

    private val REQUEST_PERMISSIONS = 1
    private val REQUEST_VIDEO_CAPTURE = 2
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        recordButton = findViewById(R.id.recordButton)
        progressBar = findViewById(R.id.progressBar)
        responseTextView = findViewById(R.id.responseTextView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_MEDIA_VIDEO),
                    REQUEST_PERMISSIONS
                )
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSIONS
                )
            }
        }



        setupObservers()

        // Check for permissions
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.READ_MEDIA_VIDEO)
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(READ_EXTERNAL_STORAGE)
            }
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(android.Manifest.permission.CAMERA)
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), REQUEST_PERMISSIONS)
        } else {
            setupAction() // Permissions already granted
        }

    }

    // This method handles the result of permission requests
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, proceed with video capture
                Log.d(TAG, "onRequestPermissionsResult: Permissions granted, proceeding with video recording")
                setupAction()
            } else {
                Log.d(TAG, "onRequestPermissionsResult: Permissions denied")
                Toast.makeText(this, "Permissions are required to access storage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Set up the button action to start video capture
    private fun setupAction() {
        Log.d(TAG, "setupAction: Setting up video recording action")
        recordButton.setOnClickListener {
            // Start video recording
            Log.d(TAG, "setupAction: Video recording started")
            recordVideo()
        }
    }

    // Start video recording with specified constraints
    private fun recordVideo() {
        Log.d(TAG, "recordVideo: Starting video recording")
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10)  // Max duration: 10 seconds
            putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)    // Lower quality (15 fps)
        }
        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE)
    }

    // Handle the result of video capture
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: Video recorded successfully")
            val videoUri: Uri? = data?.data
            videoUri?.let {
                val file = File(getExternalFilesDir(Environment.DIRECTORY_MOVIES), "video.mp4")
                if (!file.exists()) {
                    file.createNewFile()
                }
// Use the `file` object for reading/writing

                uploadVideo(file)
            }
        }
    }

    // Convert URI to file path
    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, arrayOf(MediaStore.Video.Media.DATA), null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        val filePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return filePath ?: ""
    }


    // Upload the video using ViewModel
    private fun uploadVideo(file: File) {
        Log.d(TAG, "uploadVideo: Uploading video: ${file.absolutePath}")
        viewModel.uploadVideo(file)
    }

    // Set up the observers for upload result
    private fun setupObservers() {
        viewModel.uploadResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Show progress bar while uploading
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    // Hide progress bar and show server response
                    progressBar.visibility = View.GONE
                    responseTextView.text = "Upload Successful: ${result.data.translateResult?.text}"
                }
                is Result.Error -> {
                    // Hide progress bar and show error message
                    progressBar.visibility = View.GONE
                    responseTextView.text = "Error: ${result.error}"
                    Toast.makeText(this, "Upload Failed: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}