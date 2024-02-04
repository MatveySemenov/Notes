package com.example.notes.DataBase

import androidx.lifecycle.LiveData

//Класс репозитория который принимает интерфейс DaoNotes
class NotesRepository(private val notesDao: DaoNotes) {
    val allNotes: LiveData<List<EntityDataBase>> = notesDao.getAllNotes()
    val getAllArchivedNotes: LiveData<List<EntityDataBase>> = notesDao.getAllArchivedNotes()
    val getAllDeleteNotes: LiveData<List<EntityDataBase>> = notesDao.getAllDeleteNotes()

    suspend fun insert(note: EntityDataBase){
        notesDao.insert(note)
    }

    suspend fun delete(note: EntityDataBase){
        notesDao.delete(note)
    }

    suspend fun update(note: EntityDataBase){
        notesDao.update(note.id, note.title, note.note, note.date, note.isArchived, note.isDelete)
    }

}