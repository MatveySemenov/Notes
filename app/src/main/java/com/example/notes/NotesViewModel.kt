package com.example.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.data.FirebaseNotesRepositoryImpl
import com.example.notes.data.NotesRepositoryImpl
import com.example.notes.data.databaseFirebase.NoteFirebase
import com.example.notes.data.database.EntityDataBase
import com.example.notes.data.database.NotesDataBase
import com.example.notes.domain.models.NotesDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {
    //Локальная база данных
    private val repository: NotesRepositoryImpl
    val allNotes: LiveData<List<NotesDomain>>
    val getAllArchivedNotes: LiveData<List<NotesDomain>>
    val getAllDeleteNotes: LiveData<List<NotesDomain>>

    //Серверная база данных
    private val firebaseNotesRepository: FirebaseNotesRepositoryImpl
    val firebaseNotes: LiveData<List<NoteFirebase>>
    val getAllArchivedNotesFirebase: LiveData<List<NoteFirebase>>
    val getAllDeleteNotesFirebase: LiveData<List<NoteFirebase>>

    init {
        val dao = NotesDataBase.getDataBase(application).notesDao()
        repository = NotesRepositoryImpl(dao)
        allNotes = repository.allNotes
        getAllArchivedNotes = repository.getAllArchivedNotes
        getAllDeleteNotes = repository.getAllDeleteNotes

        // Инициализируем Firebase Repository
        firebaseNotesRepository = FirebaseNotesRepositoryImpl()
        firebaseNotes = firebaseNotesRepository.firebaseNotes
        getAllArchivedNotesFirebase = firebaseNotesRepository.getAllArchivedNotesFirebase
        getAllDeleteNotesFirebase = firebaseNotesRepository.getAllDeleteNotesFirebase

    }

    //операции с локальной базой данных
    fun insertNote(note: NotesDomain) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(note)
    }

    fun updateNote(note: NotesDomain) = viewModelScope.launch(Dispatchers.IO){
        repository.update(note)
    }

    fun deleteNote(note: NotesDomain) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(note)
    }



    //операции с Firebase
    fun insertFirebase(note: NoteFirebase) = viewModelScope.launch(Dispatchers.IO) {
        firebaseNotesRepository.insertFirebaseNote(note)
    }

    fun updateFirebaseNote(note: NoteFirebase) = viewModelScope.launch(Dispatchers.IO) {
        firebaseNotesRepository.updateFirebaseNote(note)
    }

    fun deleteFirebaseNote(note: NoteFirebase) = viewModelScope.launch(Dispatchers.IO) {
        firebaseNotesRepository.deleteFirebaseNote(note)
    }

}