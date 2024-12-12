package com.fadhly.gestura.ui.textToSign

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.fadhly.gestura.R
import com.fadhly.gestura.data.Result
import com.fadhly.gestura.databinding.ActivityTextToSignBinding
import com.fadhly.gestura.ui.ViewModelFactory

class TextToSignActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextToSignBinding
    private val viewModel by viewModels<TextToSignViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextToSignBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        binding.btnTranslate.setOnClickListener {
            val text = binding.tilType.editText?.text.toString().trim()

            if (text.isEmpty()) {
                // Set an error message on the TextInputLayout or EditText
                binding.tilType.error = "Text cannot be empty"
            } else {
                // Clear any previous error and proceed
                binding.tilType.error = null
                viewModel.uploadText(text)
            }
        }

        viewModel.videoResponse.observe(this) {
            when (it) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Toast.makeText(this, "Upload Failed: ${it.error}", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("TextToSignActivity", "Error: ${it.error}")
                }

                is Result.Success -> {
                    val videoFile = it.data
                    val uri = Uri.fromFile(videoFile)
                    binding.vvSign.apply {
                        setVideoURI(uri)
                        setOnPreparedListener {
                            binding.progressBar.visibility = View.GONE
                            start()
                        }
                        setOnCompletionListener {
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}