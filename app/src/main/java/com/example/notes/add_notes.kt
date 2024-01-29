package com.example.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.Adaptor.NotesAdaptor
import com.example.notes.DataBase.EntityDataBase
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
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        try{
            oldNote = intent.getSerializableExtra("current_note") as EntityDataBase
            binding.etTitle.setText(oldNote.title)
            binding.etNote.setText(oldNote.note)
            isUpdate = true
        }catch (e:Exception){
            e.printStackTrace()
        }
        if (isUpdate){
            binding.imgDelete.visibility = View.VISIBLE
        }
        else{
            binding.imgDelete.visibility = View.INVISIBLE
        }

        binding.imgCheck.setOnClickListener {
            saveNote()
        }

        binding.imgDelete.setOnClickListener {
            val intent = Intent()
            intent.putExtra("note",oldNote)
            intent.putExtra("delete_note", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
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
                    EntityDataBase(oldNote.id,title,noteText,data.format(Date()))
                } else{
                    EntityDataBase(null, title, noteText, data.format(Date()))
                }
                val intent = Intent()
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK, intent)
            } else{
                saveNoteToFirebase(title, noteText, data.format(Date()))
            }
            finish()
        }
        else{
            Toast.makeText(this@add_notes,"Введите данные", Toast.LENGTH_LONG).show()
        }
    }


    private fun saveNoteToFirebase(title: String, text: String, date: String){
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        if (uid != null){
            val noteRef = FirebaseDatabase.getInstance().getReference("notes").child(uid).push()
            val noteId = noteRef.push().key
            val note = NoteFirebase(noteId!!,title, text, date)

            noteRef.setValue(note).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this@add_notes,"Заметка успешно сохранена", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@add_notes, "Ошибка сохранения заметки", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
