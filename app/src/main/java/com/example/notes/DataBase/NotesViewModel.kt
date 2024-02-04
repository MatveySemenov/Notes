package com.example.notes.DataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.DataBase.Firebase.FirebaseNotesRepository
import com.example.notes.ListUser.NoteFirebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {
    private val repository: NotesRepository
    private val firebaseNotesRepository: FirebaseNotesRepository
    val allNotes: LiveData<List<EntityDataBase>>
    val getAllArchivedNotes: LiveData<List<EntityDataBase>>
    val getAllDeleteNotes: LiveData<List<EntityDataBase>>
    val firebaseNotes: LiveData<List<NoteFirebase>>

    init {
        val dao = NotesDataBase.getDataBase(application).notesDao()
        repository = NotesRepository(dao)
        allNotes = repository.allNotes
        getAllArchivedNotes = repository.getAllArchivedNotes
        getAllDeleteNotes = repository.getAllDeleteNotes

        // Инициализируем Firebase Repository
        firebaseNotesRepository = FirebaseNotesRepository()
        firebaseNotes = firebaseNotesRepository.firebaseNotes

    }

    //операции с локальной базой данных
    fun insertNote(note: EntityDataBase) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(note)
    }

    fun updateNote(note: EntityDataBase) = viewModelScope.launch(Dispatchers.IO){
        repository.update(note)
    }

    fun deleteNote(note: EntityDataBase) = viewModelScope.launch(Dispatchers.IO){
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