package com.example.notes.UI.settings

import android.content.Context
import android.content.SharedPreferences
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

        themeField = binding.themeField
        alertDialogBuilder = AlertDialog.Builder(this) //инициализируем

        binding.boxTheme.setOnClickListener {
            showThemeDialog()
        }

        //кнопка выхода из настроек
        binding.imgBackArrowNavSettings.setOnClickListener {
            onBackPressed()
        }

        // Проверяем сохраненную тему и устанавливаем соответствующую при загрузке активности
        selectedThemeIndex = if (getSaveThemeState()){
            setDarkTheme()
            1
        }else{
            setLightTheme()
            0
        }
        updateThemeFieldText()
    }

    // Показать диалог выбора темы
    private fun showThemeDialog(){
        val themes = arrayOf("Светлая", "Темная")
        alertDialogBuilder.setTitle("Выберите тему")
        alertDialogBuilder.setSingleChoiceItems(themes, selectedThemeIndex) { dialog, which ->
            selectedThemeIndex = which
            updateThemeFieldText()
            dialog.dismiss()
        }
        val dialog = alertDialogBuilder.create()
        dialog.show()
    }

    // Обновить текстовое поле с выбранной темой
    private fun updateThemeFieldText() {
        val themes = arrayOf("Светлая", "Темная")
        themeField.text = themes[selectedThemeIndex]

        // Установка соответствующей темы
        if (selectedThemeIndex == 0) {
            setLightTheme()
        } else {
            setDarkTheme()
        }

        // Сохранение состояния темы
        saveThemeState(selectedThemeIndex == 1)
    }

    private fun setLightTheme(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
    private fun setDarkTheme(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    // Получить сохраненное состояние темы
    private fun getSaveThemeState():Boolean{
        val sharedPreferences : SharedPreferences = getSharedPreferences("ThemePrefs",Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isDarkTheme",false)
    }

    // Сохранить состояние темы
    private fun saveThemeState(isDarkTheme:Boolean){
        val sharedPreferences : SharedPreferences = getSharedPreferences("ThemePrefs",Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("isDarkTheme",isDarkTheme)
        editor.apply()
    }

}