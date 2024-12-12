package com.fadhly.gestura.ui.result

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fadhly.gestura.R
import com.fadhly.gestura.data.Result
import com.fadhly.gestura.databinding.ActivityTextResultBinding
import com.fadhly.gestura.ui.ViewModelFactory
import com.fadhly.gestura.ui.home.HomeActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TextResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextResultBinding
    private val REQUEST_PERMISSIONS = 1
    private val REQUEST_VIDEO_CAPTURE = 2
    private lateinit var videoFile: File
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextResultBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkAndRequestPermissions()
        setupObservers()

        // Retrieve and display the translated text
        val translatedText = intent.getStringExtra("TRANSLATED_TEXT")
        binding.tvResult.text = translatedText ?: "Press the translate button"

        setupAction()

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
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
        binding.btnAgain.setOnClickListener {
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
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvResult.text = "Translating..."
                }
                is Result.Success -> {
                    val translatedText = result.data.hasilPrediksi
                    binding.progressBar.visibility = View.GONE

                    binding.tvResult.text = translatedText
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvResult.text = "Error"
                    Toast.makeText(this, "Upload Failed: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}