package com.fadhly.gestura

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fadhly.gestura.databinding.ActivitySignToText2Binding
import com.fadhly.gestura.databinding.ActivitySignToText3Binding

class SignToText2Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySignToText2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignToText2Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.pt1.setOnClickListener {
            val intent = Intent(this, SignToText1Activity::class.java)
            startActivity(intent)
        }

        binding.pt3.setOnClickListener {
            val intent = Intent(this, SignToText3Activity::class.java)
            startActivity(intent)
        }

        binding.viewNext.setOnClickListener {
            val intent = Intent(this, SignToText3Activity::class.java)
            startActivity(intent)
        }

        binding.viewBack.setOnClickListener {
            val intent = Intent(this, SignToText1Activity::class.java)
            startActivity(intent)
        }

    }
}