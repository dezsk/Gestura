package com.fadhly.gestura.ui.onBoarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fadhly.gestura.ui.Page

class OnBoardingAdapter2(activity: FragmentActivity, private val pagerList: ArrayList<Page>) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return pagerList.size
    }

    override fun createFragment(position: Int): Fragment {
        return OnBoardingFragment(pagerList[position])
    }
}