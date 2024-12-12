package com.fadhly.gestura.ui.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.fadhly.gestura.R
import com.fadhly.gestura.ui.Page


class OnBoardingFragment(val page: Page) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_boarding, container, false)

        val tvTitle = view.findViewById<TextView>(R.id.frag_tv_title)
        val tvDesc = view.findViewById<TextView>(R.id.frag_tv_subtitle)
        val ivBackground = view.findViewById<ImageView>(R.id.frag_background)

        tvTitle.text = page.title
        tvDesc.text = page.description
        ivBackground.setImageResource(page.image)
        return view
    }

}