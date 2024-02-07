package com.example.notes.presentation

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View

class ExitApp(private val activity: Activity) {

    private var doubleBackToExitPressedOnce = false

    fun setupBackPressHandler(view: View){
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (doubleBackToExitPressedOnce) {
                    activity.finishAffinity()
                } else {
                    doubleBackToExitPressedOnce = true

                    Handler(Looper.getMainLooper()).postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 2000)
                }
                return@setOnKeyListener true
            }
            false
        }
    }
}