package com.example.notes.Fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.notes.R
import com.google.firebase.auth.FirebaseAuth

class loading_app : Fragment(){

    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.loading_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        val isLogin: Boolean = mAuth.currentUser != null

        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({
            if (isLogin){
                navController.navigate(R.id.action_loading_app_to_notes_book)
            }
            else{
                navController.navigate(R.id.action_loading_app_to_sign_in)
            }
        }, 2000)
    }

    private fun init(view: View){
        mAuth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
    }
}