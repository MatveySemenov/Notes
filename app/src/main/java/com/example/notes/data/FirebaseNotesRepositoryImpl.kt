package com.example.notes.data

import androidx.lifecycle.MutableLiveData
import com.example.notes.data.databaseFirebase.NoteFirebase
import com.example.notes.domain.FirebaseNotesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseNotesRepositoryImpl: FirebaseNotesRepository {

    override val firebaseNotes = MutableLiveData<List<NoteFirebase>>()
    override val getAllArchivedNotesFirebase = MutableLiveData<List<NoteFirebase>>()
    override val getAllDeleteNotesFirebase = MutableLiveData<List<NoteFirebase>>()

     override suspend fun insertFirebaseNote(note: NoteFirebase) {
         val user = FirebaseAuth.getInstance().currentUser
         val uid = user?.uid
         if (uid != null){
             val noteRef = FirebaseDatabase.getInstance().getReference("notes").child(uid).push()
             val noteKey = noteRef.key
             note.id = noteKey ?: ""
             noteRef.setValue(note)
         }
    }

    override suspend fun updateFirebaseNote(note: NoteFirebase){
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val uid = it.uid
            val notesRef = FirebaseDatabase.getInstance().getReference("notes").child(uid).child(note.id!!)
            notesRef.setValue(note)
        }
    }

    override suspend fun deleteFirebaseNote(note: NoteFirebase) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val uid = it.uid
            val notesRef = FirebaseDatabase.getInstance().getReference("notes").child(uid).child(
                note.id!!
            )
            notesRef.removeValue()
        }
    }
}