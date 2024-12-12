package com.fadhly.gestura.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fadhly.gestura.R
import com.fadhly.gestura.databinding.ActivityHomeBinding
import com.fadhly.gestura.ui.dictionary.DictionaryActivity
import com.fadhly.gestura.ui.onBoarding.OnBoarding2Activity
import com.fadhly.gestura.ui.signin.SignInActivity
import com.fadhly.gestura.ui.textToSign.TextToSignActivity
import com.fadhly.gestura.ui.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.camera.setOnClickListener {
            val intent = Intent(this, OnBoarding2Activity::class.java)
            startActivity(intent)
        }
        binding.tvDictionary.setOnClickListener {
            val intent = Intent(this, DictionaryActivity::class.java)
            startActivity(intent)
        }
        binding.tvText.setOnClickListener {
            val intent = Intent(this, TextToSignActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }
}