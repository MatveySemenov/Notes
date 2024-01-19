package com.example.notes.UI.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.notes.databinding.NavAboutBinding

class nav_about: AppCompatActivity() {

    private lateinit var binding: NavAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NavAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBackArrowNavAbout?.setOnClickListener{
            onBackPressed()
        }
    }
}