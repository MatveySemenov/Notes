package com.example.notes.DataBase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {
    private val repository: NotesRepository
    val allNotes: LiveData<List<EntityDataBase>>

    init {
        val dao = NotesDataBase.getDataBase(application).notesDao()
        repository = NotesRepository(dao)
        allNotes = repository.allNotes
    }

    fun  insertNote(note: EntityDataBase) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(note)
    }

    fun updateNote(note: EntityDataBase) = viewModelScope.launch(Dispatchers.IO){
        repository.update(note)
    }

    fun deleteNote(note: EntityDataBase) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(note)
    }
}