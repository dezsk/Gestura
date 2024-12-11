package com.fadhly.gestura.ui.signToText

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fadhly.gestura.R
import com.fadhly.gestura.databinding.ActivitySignToText1Binding
import com.fadhly.gestura.ui.home.HomeActivity

class SignToText1Activity : AppCompatActivity() {
    private lateinit var binding: ActivitySignToText1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignToText1Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.pt2.setOnClickListener {
            val intent = Intent(this, SignToText2Activity::class.java)
            startActivity(intent)
        }

        binding.pt3.setOnClickListener {
            val intent = Intent(this, SignToText3Activity::class.java)
            startActivity(intent)
        }

        binding.viewNext.setOnClickListener {
            val intent = Intent(this, SignToText2Activity::class.java)
            startActivity(intent)
        }

        binding.viewBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}