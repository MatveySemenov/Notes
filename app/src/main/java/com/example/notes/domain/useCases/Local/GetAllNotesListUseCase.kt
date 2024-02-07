package com.example.notes.domain.useCases.Local

import androidx.lifecycle.LiveData
import com.example.notes.domain.NotesRepository
import com.example.notes.domain.models.NotesDomain

class GetAllNotesListUseCase(private val repository: NotesRepository) {
    fun executeNotes(): LiveData<List<NotesDomain>> {
        return repository.allNotes
    }

    fun executeArchived(): LiveData<List<NotesDomain>>{
        return repository.getAllArchivedNotes
    }

    fun executeDelete(): LiveData<List<NotesDomain>>{
        return repository.getAllDeleteNotes
    }
}