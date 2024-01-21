package com.example.notes.UI.settings

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.notes.databinding.NavSettingsBinding

class nav_settings: AppCompatActivity() {

    private lateinit var binding: NavSettingsBinding
    private lateinit var themeField: TextView
    private var selectedThemeIndex = 0
    private lateinit var alertDialogBuilder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NavSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        themeField = binding.themeField!!
        alertDialogBuilder = AlertDialog.Builder(this) //инициализируем

        binding.boxTheme?.setOnClickListener {
            showThemeDialog()
        }

        binding.imgBackArrowNavSettings?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showThemeDialog(){
        val themes = arrayOf("Светлая", "Темная")
        alertDialogBuilder.setTitle("Выберите тему")
        alertDialogBuilder.setSingleChoiceItems(themes, selectedThemeIndex) { dialog, which ->
            selectedThemeIndex = which
            val selectedTheme = themes[which]
            themeField.text = selectedTheme
            handleThemeChange() //обработчик изменения темы
            dialog.dismiss()
        }
        val dialog = alertDialogBuilder.create()
        dialog.show()
    }

    private fun handleThemeChange(){
        when (selectedThemeIndex){
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}