package com.fadhly.gestura

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fadhly.gestura.databinding.ActivitySignToText3Binding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SignToText3Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySignToText3Binding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val REQUEST_PERMISSIONS = 1
    private val REQUEST_VIDEO_CAPTURE = 2
    private val TAG = "MainActivity"
    private lateinit var videoFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignToText3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAndRequestPermissions()
        setupObservers()

        binding.pt1.setOnClickListener {
            val intent = Intent(this, SignToText1Activity::class.java)
            startActivity(intent)
        }

        binding.viewBack.setOnClickListener {
            val intent = Intent(this, SignToText2Activity::class.java)
            startActivity(intent)
        }

        binding.pt2.setOnClickListener {
            val intent = Intent(this, SignToText2Activity::class.java)
            startActivity(intent)
        }
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                ),
                REQUEST_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupAction()
        } else {
            Toast.makeText(this, "Permissions are required to proceed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAction() {
        binding.viewNext.setOnClickListener {
            recordVideo()
        }
    }

    private fun recordVideo() {
        videoFile = createVideoFile()
        val videoUri: Uri = FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            videoFile
        )

        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
            putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10) // Max duration: 10 seconds
            putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0) // Lower quality
        }

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_VIDEO_CAPTURE)
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createVideoFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES)

        return File.createTempFile(
            "VIDEO_${timeStamp}_", /* prefix */
            ".mp4", /* suffix */
            storageDir /* directory */
        )
    }

    @Deprecated("Use Activity Result API instead")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            uploadVideo(videoFile)
        } else {
            Toast.makeText(this, "Video capture canceled.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadVideo(file: File) {
        viewModel.uploadVideo(file)
    }

    private fun setupObservers() {
        viewModel.uploadResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val translatedText = result.data.translateResult?.text
                    navigateToResultActivity(translatedText)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Upload Failed: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun navigateToResultActivity(translatedText: String?) {
        val intent = Intent(this, TextResultActivity::class.java).apply {
            putExtra("TRANSLATED_TEXT", translatedText)
        }
        startActivity(intent)
    }
}
