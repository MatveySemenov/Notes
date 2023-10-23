package com.example.notes

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.Adaptor.NotesAdaptor
import com.example.notes.DataBase.EntityDataBase
import com.example.notes.DataBase.NotesDataBase
import com.example.notes.DataBase.NotesViewModel
import com.example.notes.Fragments.sign_in
import com.example.notes.R
import com.example.notes.databinding.NotesBookBinding
import com.example.notes.databinding.SignInBinding
import com.example.notes.databinding.SignUpBinding
import com.google.firebase.auth.FirebaseAuth

class notes_book : AppCompatActivity(), NotesAdaptor.NoteClickListener{
    private lateinit var binding: NotesBookBinding
    private lateinit var database: NotesDataBase
    lateinit var viewModel: NotesViewModel
    lateinit var adapter: NotesAdaptor

    private var doubleBackToExitPressedOnce = false

    //выход из приложения
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity() // Завершить все активности приложения
        } else {
            this.doubleBackToExitPressedOnce = true

            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotesBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()


        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NotesViewModel::class.java)

        viewModel.allNotes.observe(this){list ->
            list?.let {
                adapter.updateList(list)
            }
        }
        database = NotesDataBase.getDataBase(this)
    }

    private fun initUI(){

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        adapter = NotesAdaptor(this,this)
        binding.recyclerView.adapter = adapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
                if(result.resultCode == Activity.RESULT_OK){
                    val note = result.data?.getSerializableExtra("note") as? EntityDataBase
                    if(note != null){
                        viewModel.insertNote(note)
                    }
                }
            }

        binding.addNote.setOnClickListener {
            val intent = Intent(this, add_notes::class.java)
            getContent.launch(intent)
        }

    }

    private val UpdateOrDeleteNote =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if(result.resultCode == Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra("note") as EntityDataBase
                val isDelete = result.data?.getBooleanExtra("delete_note", false) as Boolean
                if(note != null && !isDelete){
                    viewModel.updateNote(note)
                }
                else if(note != null && isDelete){
                    viewModel.deleteNote(note)
                }
            }
        }
    override fun onNoteClicked(note: EntityDataBase){
        val intent = Intent(this@notes_book,add_notes::class.java)
        intent.putExtra("current_note", note)
        UpdateOrDeleteNote.launch(intent)
    }
}