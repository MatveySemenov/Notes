package com.example.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notes.DataBase.EntityDataBase
import com.example.notes.DataBase.NotesViewModel
import com.example.notes.ListUser.NoteFirebase
import com.example.notes.databinding.AddNotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class add_notes : AppCompatActivity(){

    private lateinit var binding: AddNotesBinding
    private lateinit var note: EntityDataBase
    private lateinit var oldNote: EntityDataBase

    private lateinit var noteFirebase: NoteFirebase
    private lateinit var oldNoteFirebase: NoteFirebase
    private lateinit var viewModel: NotesViewModel


    private var isUpdate = false
    private var isArchived: Boolean = false
    private var isDelete: Boolean = false

    private val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)  // Инициализируйте ViewModel

        try{
            if (currentUser != null){
                oldNoteFirebase = intent.getSerializableExtra("firebase_note") as NoteFirebase
                binding.etTitle.setText(oldNoteFirebase.title)
                binding.etNote.setText(oldNoteFirebase.text)
                isUpdate = true
                isArchived = oldNoteFirebase.isArchived
            } else {
                oldNote = intent.getSerializableExtra("current_note") as EntityDataBase
                binding.etTitle.setText(oldNote.title)
                binding.etNote.setText(oldNote.note)
                isUpdate = true
                isArchived = oldNote.isArchived
                isDelete = oldNote.isDelete
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

        if (isUpdate){
            binding.imgDelete.visibility = View.VISIBLE

            if (isDelete){
                binding.imgCheck.visibility = View.GONE
                binding.imgArchive.visibility = View.GONE
                binding.imgDelete.setImageResource(R.drawable.delete_forever)
                binding.imgRestoreDelete?.visibility = View.VISIBLE
            } else {
                binding.imgDelete.setImageResource(R.drawable.delete)
                binding.imgRestoreDelete?.visibility = View.INVISIBLE
            }

        } else{
            binding.imgDelete.visibility = View.INVISIBLE
            binding.imgRestoreDelete?.visibility = View.INVISIBLE
        }


        if (isArchived){
            binding.imgArchive.setImageResource(R.drawable.unarchive)
        } else {
            binding.imgArchive.setImageResource(R.drawable.archive)
        }


        binding.imgRestoreDelete?.setOnClickListener {
            isDelete = !isDelete
            saveNote()
        }

        binding.imgCheck.setOnClickListener {
            saveNote()
        }


        binding.imgDelete.setOnClickListener {
            if (isDelete){
                val intent = Intent()
                if (currentUser != null){
                    intent.putExtra("noteFirebase", oldNoteFirebase)
                    intent.putExtra("delete_noteFirebase", true)
                } else {
                    intent.putExtra("note",oldNote)
                    intent.putExtra("delete_note", true)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                isDelete = !isDelete
                saveNote()
            }
        }

        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }

        binding.imgArchive.setOnClickListener {
            isArchived = !isArchived
            saveNote()
        }

    }

    private fun saveNote(){
        val title = binding.etTitle.text.toString()
        val noteText = binding.etNote.text.toString()
        val data = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

        if(title.isNotEmpty() || noteText.isNotEmpty()){

            val user = FirebaseAuth.getInstance().currentUser
            if (user == null){
                note = if (isUpdate){
                    EntityDataBase(oldNote.id,title,noteText,data.format(Date()),isArchived,isDelete)
                } else{
                    EntityDataBase(null, title, noteText, data.format(Date()),isArchived,isDelete)
                }
                val intent = Intent()
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK, intent)
            } else {
                noteFirebase = if (isUpdate) {
                    NoteFirebase(oldNoteFirebase.id,title, noteText, data.format(Date()),isArchived)
                } else {
                    NoteFirebase("",title, noteText, data.format(Date()),isArchived)
                }
                val intent = Intent()
                intent.putExtra("noteFirebase", noteFirebase)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
        else{
            Toast.makeText(this@add_notes,"Введите данные", Toast.LENGTH_LONG).show()
        }
    }
}
