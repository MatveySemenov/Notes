package com.example.notes.domain.useCases.Local

import com.example.notes.domain.NotesRepository
import com.example.notes.domain.models.NotesDomain

class InsertToDBUseCase(private val repository: NotesRepository) {
    suspend fun executeInsert(notesDomain: NotesDomain){
        repository.insert(notesDomain)
    }
}