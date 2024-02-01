package com.example.notes.UI.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.databinding.NavAboutBinding

class NavAbout: AppCompatActivity() {

    private lateinit var binding: NavAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NavAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBackArrowNavAbout.setOnClickListener{
            onBackPressed()
        }
    }
}