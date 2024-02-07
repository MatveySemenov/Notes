package com.example.notes.presentation.addNotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notes.R
import com.example.notes.data.databaseFirebase.NoteFirebase
import com.example.notes.databinding.AddNotesBinding
import com.example.notes.domain.models.NotesDomain
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddNotes : AppCompatActivity(){

    private lateinit var binding: AddNotesBinding
    private val viewModel: AddNotesViewModel by viewModels()
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var isUpdate = false
    private var isArchived: Boolean = false
    private var isDelete: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try{
            if (currentUser != null){
                val oldNoteFirebase = intent.getSerializableExtra("firebase_note") as NoteFirebase
                viewModel.setNoteFirebaseData(oldNoteFirebase)
                binding.etTitle.setText(oldNoteFirebase.title)
                binding.etNote.setText(oldNoteFirebase.text)
                isUpdate = true
                isArchived = oldNoteFirebase.isArchived
                isDelete = oldNoteFirebase.isDelete
            } else {
                val oldNote = intent.getSerializableExtra("current_note") as NotesDomain
                viewModel.setNoteData(oldNote)
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
            deleteNote()
        }

        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }

        binding.imgArchive.setOnClickListener {
            isArchived = !isArchived
            saveNote()
        }

    }

    private fun deleteNote(){
        if (isDelete){
            val oldNote = viewModel.note.value
            val oldNoteFirebase = viewModel.noteFirebase.value
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

    private fun saveNote(){
        val title = binding.etTitle.text.toString()
        val noteText = binding.etNote.text.toString()
        val data = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

        if(title.isNotEmpty() || noteText.isNotEmpty()){

            val user = FirebaseAuth.getInstance().currentUser
            if (user == null){
                if (isUpdate){
                    val updateNotes = NotesDomain(viewModel.note.value?.id,title,noteText,data.format(Date()),isArchived,isDelete)
                    viewModel.setNoteData(updateNotes)
                } else {
                    val newNotes = NotesDomain(null, title, noteText, data.format(Date()),isArchived,isDelete)
                    viewModel.setNoteData(newNotes)
                }
                val intent = Intent()
                intent.putExtra("note",viewModel.note.value)
                setResult(Activity.RESULT_OK, intent)
            } else {
                if (isUpdate) {
                    val updateNotesFirebase = NoteFirebase(viewModel.noteFirebase.value?.id,title, noteText, data.format(Date()),isArchived,isDelete)
                    viewModel.setNoteFirebaseData(updateNotesFirebase)
                } else {
                    val newNotesFirebase = NoteFirebase("",title, noteText, data.format(Date()),isArchived,isDelete)
                    viewModel.setNoteFirebaseData(newNotesFirebase)
                }
                val intent = Intent()
                intent.putExtra("noteFirebase", viewModel.noteFirebase.value)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
        else{
            Toast.makeText(this@AddNotes,"Введите данные", Toast.LENGTH_LONG).show()
        }
    }
}
