package com.example.notes

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.notes.DataBase.EntityDataBase
import com.example.notes.R
import com.example.notes.databinding.AddNotesBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class add_notes : AppCompatActivity(){

    private lateinit var binding: AddNotesBinding
    private lateinit var note: EntityDataBase
    private lateinit var oldNote: EntityDataBase
    var isUpdate = false

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
            val title = binding.etTitle.text.toString()
            val noteText = binding.etNote.text.toString()

            if(title.isNotEmpty() || noteText.isNotEmpty()){
                val data = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                if(isUpdate){
                    note = EntityDataBase(oldNote.id,title,noteText,data.format(Date()))
                }
                else{
                    note = EntityDataBase(null, title, noteText, data.format(Date()))
                }

                var intent = Intent()
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            else{
                Toast.makeText(this@add_notes,"Введите данные", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
        }

        binding.imgDelete.setOnClickListener {
            var intent = Intent()
            intent.putExtra("note",oldNote)
            intent.putExtra("delete_note", true)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }

    }
}