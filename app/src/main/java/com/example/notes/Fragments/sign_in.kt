package com.example.notes.Fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.databinding.SignInBinding
import com.google.firebase.auth.FirebaseAuth


class sign_in() : Fragment(){

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: SignInBinding

    private var doubleBackToExitPressedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = SignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (doubleBackToExitPressedOnce) {
                    activity?.finishAffinity() // Завершить все активности приложения
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

        //

        init(view)

        binding.registration.setOnClickListener {
            navController.navigate(R.id.action_sign_in_to_sign_up)
        }

        var isProcessing = false

        binding.nextButton.setOnClickListener {
            if (isProcessing) {
                return@setOnClickListener
            }

            isProcessing = true

            val email = binding.emailIn.text.toString()
            val password = binding.passwordIn.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                Autentification(email,password)
            }
            else{
                Toast.makeText(context, "Введите логин и пароль",Toast.LENGTH_SHORT).show()
            }
            Handler().postDelayed({
                isProcessing = false
            }, 2000)
        }
    }

    private fun Autentification(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                navController.navigate(R.id.action_sign_in_to_notes_book)
            }
            else{
                Toast.makeText(context,"Пользователь не зарегистрирован",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
    }
}

