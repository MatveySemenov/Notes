package com.example.notes.domain.useCases.Server

import com.example.notes.data.FirebaseNotesRepositoryImpl
import com.example.notes.data.databaseFirebase.NoteFirebase
import com.example.notes.domain.FirebaseNotesRepository

class FirebaseDeleteFromDBUseCase(private val repository: FirebaseNotesRepository) {
    suspend fun firebaseExecute(noteFirebase: NoteFirebase){
        repository.deleteFirebaseNote(noteFirebase)
    }
}