package com.example.notes.domain.useCases.Server

import com.example.notes.data.databaseFirebase.NoteFirebase
import com.example.notes.domain.FirebaseNotesRepository

class FirebaseInsertToDBUseCase(private val repository: FirebaseNotesRepository) {
    suspend fun firebaseExecuteInsert(noteFirebase: NoteFirebase){
        repository.insertFirebaseNote(noteFirebase)
    }
}