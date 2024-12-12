package com.fadhly.gestura.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fadhly.gestura.R
import com.fadhly.gestura.databinding.ActivitySplashBinding
import com.fadhly.gestura.ui.home.HomeActivity
import com.fadhly.gestura.ui.onBoarding.OnBoardingActivity
import com.fadhly.gestura.ui.result.TextResultActivity
import com.fadhly.gestura.ui.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Check user login status
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // Navigate to HomeActivity
            startActivity(Intent(this, OnBoardingActivity::class.java))
        } else {
            // Navigate to WelcomeActivity
            startActivity(Intent(this, WelcomeActivity::class.java))
        }
        finish() // Close SplashActivity
    }


}