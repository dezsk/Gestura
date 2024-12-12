package com.fadhly.gestura.ui.dictionary

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.fadhly.gestura.R
import com.fadhly.gestura.data.local.SignLanguages
import com.fadhly.gestura.databinding.ActivityDictionaryBinding

class DictionaryActivity : AppCompatActivity() {
private lateinit var binding: ActivityDictionaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDictionaryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val adapter = SignLanguageAdapter()
        binding.rvSign.layoutManager = GridLayoutManager(this, 3)
        binding.rvSign.adapter = adapter

        adapter.addSignList(SignLanguages.signLanguageList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}