package com.example.notes.domain.useCases.Local

import com.example.notes.domain.NotesRepository
import com.example.notes.domain.models.NotesDomain

class DeleteFromDBUseCase(private val repository: NotesRepository) {
    suspend fun execute(notesDomain: NotesDomain){
        repository.delete(notesDomain)
    }
}