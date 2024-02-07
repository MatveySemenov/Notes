package com.example.notes.domain

import androidx.lifecycle.LiveData
import com.example.notes.domain.models.NotesDomain

interface NotesRepository {

    val allNotes: LiveData<List<NotesDomain>>
    val getAllArchivedNotes: LiveData<List<NotesDomain>>
    val getAllDeleteNotes: LiveData<List<NotesDomain>>

    suspend fun insert(notesDomain: NotesDomain)
    suspend fun delete(notesDomain: NotesDomain)
    suspend fun update(notesDomain: NotesDomain)
}