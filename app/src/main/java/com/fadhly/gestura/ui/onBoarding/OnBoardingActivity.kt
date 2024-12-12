package com.fadhly.gestura.ui.onBoarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.fadhly.gestura.R
import com.fadhly.gestura.databinding.ActivityOnBoardingBinding
import com.fadhly.gestura.ui.Page
import com.fadhly.gestura.ui.home.HomeActivity
import com.fadhly.gestura.ui.signin.SignInActivity
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding

    private val onBoardingPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            when(position){
                0 -> {
                    binding.viewBack.visibility = View.GONE
                    binding.viewNext.visibility = View.VISIBLE
                    binding.tvNext.text = "Next"
                }
                1 -> {
                    binding.viewBack.visibility = View.VISIBLE
                    binding.viewNext.visibility = View.VISIBLE
                    binding.tvNext.text = "Next"
                }
                2 -> {
                    binding.viewBack.visibility = View.VISIBLE
                    binding.viewNext.visibility = View.VISIBLE
                    binding.tvNext.text = "Next"
                }
                else -> {
                    binding.viewBack.visibility = View.VISIBLE
                    binding.viewNext.visibility = View.VISIBLE
                    binding.tvNext.text = "Start"
                }

            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val pagerList = arrayListOf(
            Page(
                "Sign Language Interpreter Applicaion",
                "An Application for learning and translating sign language. Helps to promote and share sign language knowledge for communicating with deaf individuals, reducing communication barriers and raising equality.",
                R.drawable.boy
            ),
            Page(
                "Sign Language Interpreter Applicaion",
                "Learn and step up your sign language knowledge with a list of sign language vocabularies.",
                R.drawable.girl
            ),
            Page(
                "Sign Language Interpreter Applicaion",
                "Type a word or sentence, then get the sign language interpretation in order.",
                R.drawable.two_boys
            ),
            Page(
                "Sign Language Interpreter Applicaion",
                "Test your knowledge by recording yourself using sign language and translating it to your known language",
                R.drawable.children_hand
            )

        )

        binding.vpOnboarding.apply {
            adapter = OnBoardingAdapter(this@OnBoardingActivity, pagerList)
            registerOnPageChangeCallback(onBoardingPageChangeCallback)
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        TabLayoutMediator(binding.tabLayout, binding.vpOnboarding) { tab, position -> }.attach()

        binding.viewNext.setOnClickListener {
            if (binding.vpOnboarding.currentItem < binding.vpOnboarding.adapter!!.itemCount - 1) {
                binding.vpOnboarding.currentItem += 1
            } else {
                homeScreenIntent()
            }
        }
        binding.tvSkip.setOnClickListener {
            homeScreenIntent()
        }
        binding.viewBack.setOnClickListener {
            if (binding.vpOnboarding.currentItem > 0) {
                binding.vpOnboarding.currentItem -= 1
            }
        }
    }

    override fun onDestroy() {
        binding.vpOnboarding.unregisterOnPageChangeCallback(onBoardingPageChangeCallback)
        super.onDestroy()
    }
    private fun homeScreenIntent() {
        val homeIntent = Intent(this, SignInActivity::class.java)
        startActivity(homeIntent)
    }
}