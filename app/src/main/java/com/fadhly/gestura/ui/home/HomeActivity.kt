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
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        val user = auth.currentUser

        if (user !== null) {
            val username = user.displayName
            binding.tvUser.text = username
        } else {
            binding.tvUser.text = "Guest"
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            saveLoginState(false)  // Clear login state
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Handle Home navigation
                    true
                }

                R.id.navigation_text_to_sign -> {
                    // Handle Text to Sign navigation
                    startActivity(Intent(this, TextToSignActivity::class.java))
                    true
                }

                R.id.navigation_sign_to_text -> {
                    // Handle Sign to Text navigation
                    startActivity(Intent(this, OnBoarding2Activity::class.java))
                    true
                }

                R.id.navigation_dictionary -> {
                    startActivity(Intent(this, DictionaryActivity::class.java))
                    true
                }

                else -> false
            }
        }
    }

    private fun saveLoginState(isLoggedIn: Boolean) {
        val sharedPref = getSharedPreferences("app_preferences", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isLoggedIn", isLoggedIn)
            apply()
        }
    }
}