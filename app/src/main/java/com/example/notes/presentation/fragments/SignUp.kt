package com.example.notes.presentation.fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.notes.AppPreferences
import com.example.notes.presentation.ExitApp
import com.example.notes.data.databaseFirebase.User
import com.example.notes.R
import com.example.notes.UserDataChangeListener
import com.example.notes.databinding.SignUpBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUp : Fragment() {

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: SignUpBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var exitApp: ExitApp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val appPreferences = AppPreferences(requireContext())


        exitApp = ExitApp(requireActivity())
        exitApp.setupBackPressHandler(view)

        init(view)


        binding.entry.setOnClickListener {
            navController.navigate(R.id.action_sign_up_to_sign_in)
        }

        var isProcessing = false
        binding.nextButton.setOnClickListener {
            if (isProcessing) {
                return@setOnClickListener
            }

            isProcessing = true

            val email = binding.emailUp.text.toString()
            val password = binding.passwordUp.text.toString()
            val verifyPassword = binding.verifyPasswordUp.text.toString()
            val name = binding.NameUp.text.toString()
            progressBar = binding.progressBar

            if (email.isNotEmpty() && password.isNotEmpty() && verifyPassword.isNotEmpty() && name.isNotEmpty()) {
                if (password == verifyPassword) {
                    registrationUser(name, email, password)

                    appPreferences.saveUserData(name,email)
                    if(activity is UserDataChangeListener){
                        val userDataChangeListener = activity as UserDataChangeListener
                        userDataChangeListener.getUINavHeaderMain(name,email)
                    }

                    progressBar.visibility = View.VISIBLE
                } else {
                    Toast.makeText(context, "Пароли не сходятся", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Не оставляйте пустые поля", Toast.LENGTH_SHORT).show()
            }
            Handler().postDelayed({
                isProcessing = false
            }, 2000)

        }
    }

    private fun registrationUser(name: String, email: String, password: String) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val signInMethods = task.result?.signInMethods ?: emptyList()

                if (signInMethods.isNotEmpty()) {
                    // Пользователь уже существует, выводим сообщение об ошибке
                    Toast.makeText(
                        context,
                        "Пользователь уже существует с таким email",
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBar.visibility = View.GONE
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user =
                                User(userName = name, userPassword = password, userEmail = email)

                            FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().currentUser?.uid!!)
                                .setValue(user)
                                .addOnCompleteListener(OnCompleteListener<Void> { task ->
                                    if (task.isSuccessful) {
                                        navController.navigate(R.id.action_sign_up_to_notes_book)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Возникла ошибка",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        } else {
                            Toast.makeText(
                                context,
                                "Не удалось зарегистрировать пользователя",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        progressBar.visibility = View.GONE
                    }
                }

            } else {
                Toast.makeText(
                    context,
                    "Не удалось проверить email пользователя",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
    }
}