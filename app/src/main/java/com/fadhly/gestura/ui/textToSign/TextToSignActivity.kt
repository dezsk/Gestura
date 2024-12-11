package com.fadhly.gestura.ui.textToSign

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
        binding.btnTranslate.setOnClickListener {
            val text = binding.tilType.editText?.text.toString()
            viewModel.uploadText(text)
        }

        viewModel.imageResponse.observe(this){
            when (it) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    Toast.makeText(this, "Upload Failed: ${it.error}", Toast.LENGTH_SHORT)
                        .show()
                }
                is Result.Success -> {
                    Glide
                        .with(this)
                        .load(it.data.photoUrl)
                        .fitCenter()
                        .into(binding.ivSign)
                }
            }
        }
    }
}