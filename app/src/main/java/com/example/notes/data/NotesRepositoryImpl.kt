package com.example.notes.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.notes.domain.NotesRepository
import com.example.notes.domain.mappers.toDomainModel
import com.example.notes.domain.mappers.toEntity
import com.example.notes.data.database.DaoNotes
import com.example.notes.domain.models.NotesDomain

//Класс репозитория который принимает интерфейс DaoNotes
class NotesRepositoryImpl(private val notesDao: DaoNotes): NotesRepository {

    override val allNotes: LiveData<List<NotesDomain>> = notesDao.getAllNotes().map { notesList ->
        notesList.map { it.toDomainModel() }
    }

    override val getAllArchivedNotes: LiveData<List<NotesDomain>> = notesDao.getAllArchivedNotes().map { notesList ->
        notesList.map { it.toDomainModel() }
    }

    override val getAllDeleteNotes: LiveData<List<NotesDomain>> = notesDao.getAllDeleteNotes().map { noteList ->
        noteList.map { it.toDomainModel() }
    }


    override suspend fun insert(notesDomain: NotesDomain) {
        notesDao.insert(notesDomain.toEntity())
    }

    override suspend fun delete(notesDomain: NotesDomain) {
        notesDao.delete(notesDomain.toEntity())
    }

    override suspend fun update(notesDomain: NotesDomain) {
        notesDao.update(notesDomain.id, notesDomain.title,notesDomain.note,notesDomain.date,notesDomain.isArchived,notesDomain.isDelete)
    }

}