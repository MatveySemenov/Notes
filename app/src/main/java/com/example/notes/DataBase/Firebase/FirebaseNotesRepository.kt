package com.example.notes.DataBase.Firebase

import androidx.lifecycle.MutableLiveData
import com.example.notes.ListUser.NoteFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseNotesRepository {
    val firebaseNotes = MutableLiveData<List<NoteFirebase>>()
    val getAllArchivedNotesFirebase = MutableLiveData<List<NoteFirebase>>()
    val getAllDeleteNotesFirebase = MutableLiveData<List<NoteFirebase>>()

     fun insertFirebaseNote(note: NoteFirebase) {
         val user = FirebaseAuth.getInstance().currentUser
         val uid = user?.uid
         if (uid != null){
             val noteRef = FirebaseDatabase.getInstance().getReference("notes").child(uid).push()
             val noteKey = noteRef.key
             note.id = noteKey ?: ""
             noteRef.setValue(note)
         }
    }

    fun updateFirebaseNote(note: NoteFirebase){
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val uid = it.uid
            val notesRef = FirebaseDatabase.getInstance().getReference("notes").child(uid).child(note.id)
            notesRef.setValue(note)
        }
    }

    fun deleteFirebaseNote(note: NoteFirebase) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val uid = it.uid
            val notesRef = FirebaseDatabase.getInstance().getReference("notes").child(uid).child(note.id)
            notesRef.removeValue()
        }
    }
}