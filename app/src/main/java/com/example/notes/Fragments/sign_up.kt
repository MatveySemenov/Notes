package com.example.notes.Fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.SignUpBinding
import com.google.firebase.auth.FirebaseAuth

class sign_up : Fragment(){

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: SignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = SignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        binding.entry.setOnClickListener {
            navController.navigate(R.id.action_sign_up_to_sign_in)
        }

        binding.nextButton.setOnClickListener {
            val email = binding.emailUp.text.toString()
            val password = binding.passwordUp.text.toString()
            val verifyPassword = binding.verifyPasswordUp.text.toString()
            
            if(email.isNotEmpty() && password.isNotEmpty() && verifyPassword.isNotEmpty()){
                if(password == verifyPassword){
                    registrationUser(email, password)
                }
                else{
                    Toast.makeText(context,"Пароли не сходятся",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(context, "Не оставляйте пустые поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrationUser(email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                navController.navigate(R.id.action_sign_up_to_notes_book)
            }
            else{
                Toast.makeText(context, "Пользователь уже имеется с такими данными", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
    }
}