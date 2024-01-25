package com.example.notes.Fragments

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
import com.example.notes.*
import com.example.notes.databinding.SignInBinding
import com.google.firebase.auth.FirebaseAuth


class sign_in() : Fragment(){

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: SignInBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var exitApp: ExitApp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exitApp = ExitApp(requireActivity())
        exitApp.setupBackPressHandler(view)


        init(view)


        binding.registration.setOnClickListener {
            navController.navigate(R.id.action_sign_in_to_sign_up)
        }

        val appPreferences = AppPreferences(requireContext())

        var isProcessing = false

        binding.nextButton.setOnClickListener {
            if (isProcessing) {
                return@setOnClickListener
            }

            isProcessing = true

            val email = binding.emailIn.text.toString()
            val password = binding.passwordIn.text.toString()
            progressBar = binding.progressBarSignIn!!


            if(email.isNotEmpty() && password.isNotEmpty()){
                Autentification(email,password)

                val readData = ReadData()
                readData.readUserDataByEmail(email){ userList ->
                    for (user in userList){
                        val userName = user.userName
                        val userEmail = user.userEmail

                        appPreferences.saveUserData(userName,userEmail)
                        //Обновление UI
                        if(activity is UserDataChangeListener){
                            val userDataChangeListener = activity as UserDataChangeListener
                            userDataChangeListener.getUINavHeaderMain(userName, userEmail)
                        }
                    }

                }
                progressBar.visibility = View.VISIBLE
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
            progressBar.visibility = View.GONE
        }
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
    }
}

