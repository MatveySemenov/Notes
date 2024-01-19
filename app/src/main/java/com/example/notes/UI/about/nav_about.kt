package com.example.notes.UI.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.notes.databinding.NavAboutBinding

class nav_about: Fragment() {

    private lateinit var binding: NavAboutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NavAboutBinding.inflate(inflater,container,false)
        return binding.root
    }
}