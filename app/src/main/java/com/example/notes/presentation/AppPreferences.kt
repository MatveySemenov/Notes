package com.example.notes

import android.content.Context
import android.content.SharedPreferences

interface UserDataChangeListener{
    fun getUINavHeaderMain(userName: String? , userEmail: String?)
}

class AppPreferences(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("AppPref",Context.MODE_PRIVATE)

    private var userDataChangeListener: UserDataChangeListener? = null

    fun setUserDataChangeListener(listener: UserDataChangeListener) {
        userDataChangeListener = listener
    }

    fun saveUserData(userName: String? , userEmail: String?){
        val editor = preferences.edit()
        editor.putString("userName", userName)
        editor.putString("userEmail",userEmail)
        editor.apply()
        userDataChangeListener?.getUINavHeaderMain(userName, userEmail)
    }

    fun getUserData(): Pair<String?,String?>{
        val userName = preferences.getString("userName", "")
        val userEmail = preferences.getString("userEmail", "")
        return Pair(userName,userEmail)
    }
}