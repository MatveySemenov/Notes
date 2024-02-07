package com.example.notes.domain.useCases.Server

import com.example.notes.data.databaseFirebase.NoteFirebase
import com.example.notes.domain.FirebaseNotesRepository

class FirebaseUpdateDBUseCase(private val repository: FirebaseNotesRepository) {
    suspend fun firebaseExecuteUpdate(noteFirebase: NoteFirebase){
        return repository.updateFirebaseNote(noteFirebase)
    }
}