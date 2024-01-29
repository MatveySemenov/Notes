package com.example.notes.DataBase.Firebase

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notes.ListUser.NoteFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseNotesRepository {
    val firebaseNotes = MutableLiveData<List<NoteFirebase>>()

    suspend fun insertFirebaseNote(note: NoteFirebase) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val uid = it.uid
            val notesRef = FirebaseDatabase.getInstance().getReference("notes").child(uid)
            notesRef.push().setValue(note)
        }
    }

    suspend fun updateFirebaseNote(note: NoteFirebase){
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val uid = it.uid
            val notesRef = FirebaseDatabase.getInstance().getReference("notes").child(uid)
            notesRef.child(note.id).setValue(note)
        }
    }

    suspend fun deleteFirebaseNote(note: NoteFirebase) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val uid = it.uid
            val notesRef = FirebaseDatabase.getInstance().getReference("notes").child(uid)
            notesRef.child(note.id).removeValue()
        }
    }
}