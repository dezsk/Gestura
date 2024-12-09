package com.fadhly.gestura

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fadhly.gestura.databinding.ActivityOnBoarding2Binding
import com.fadhly.gestura.databinding.ActivityOnBoarding3Binding

class OnBoarding3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoarding3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoarding3Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.pt1.setOnClickListener {
            val intent = Intent(this, OnBoarding1Activity::class.java)
            startActivity(intent)
        }

        binding.pt2.setOnClickListener {
            val intent = Intent(this, OnBoarding2Activity::class.java)
            startActivity(intent)
        }

        binding.pt4.setOnClickListener {
            val intent = Intent(this, OnBoarding4Activity::class.java)
            startActivity(intent)
        }

        binding.viewNext.setOnClickListener {
            val intent = Intent(this, OnBoarding4Activity::class.java)
            startActivity(intent)
        }

        binding.viewBack.setOnClickListener {
            val intent = Intent(this, OnBoarding2Activity::class.java)
            startActivity(intent)
        }
    }
}