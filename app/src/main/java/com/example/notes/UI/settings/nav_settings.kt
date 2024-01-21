package com.example.notes.UI.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.databinding.NavSettingsBinding

class nav_settings: AppCompatActivity() {

    private lateinit var binding: NavSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NavSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}