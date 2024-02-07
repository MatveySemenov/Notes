package com.example.notes.presentation.addNotes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notes.data.databaseFirebase.NoteFirebase
import com.example.notes.domain.models.NotesDomain
import com.google.firebase.auth.FirebaseAuth

class AddNotesViewModel(application: Application) : AndroidViewModel(application) {

    private val _note = MutableLiveData<NotesDomain?>()
    val note: LiveData<NotesDomain?> = _note

    private val _noteFirebase = MutableLiveData<NoteFirebase?>()
    val noteFirebase: LiveData<NoteFirebase?> = _noteFirebase

    fun setNoteData(note: NotesDomain?){
        _note.value = note
    }

    fun setNoteFirebaseData(noteFirebase: NoteFirebase?){
        _noteFirebase.value = noteFirebase
    }
}