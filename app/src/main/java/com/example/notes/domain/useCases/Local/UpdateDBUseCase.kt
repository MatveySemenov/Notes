package com.example.notes.domain.useCases.Local

import com.example.notes.domain.NotesRepository
import com.example.notes.domain.models.NotesDomain

class UpdateDBUseCase(private val repository: NotesRepository) {
    suspend fun executeUpdate(notesDomain: NotesDomain){
        repository.update(notesDomain)
    }
}