package com.example.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.data.FirebaseNotesRepositoryImpl
import com.example.notes.data.NotesRepositoryImpl
import com.example.notes.data.databaseFirebase.NoteFirebase
import com.example.notes.data.database.NotesDataBase
import com.example.notes.domain.models.NotesDomain
import com.example.notes.domain.useCases.Local.DeleteFromDBUseCase
import com.example.notes.domain.useCases.Local.GetAllNotesListUseCase
import com.example.notes.domain.useCases.Local.InsertToDBUseCase
import com.example.notes.domain.useCases.Local.UpdateDBUseCase
import com.example.notes.domain.useCases.Server.FirebaseDeleteFromDBUseCase
import com.example.notes.domain.useCases.Server.FirebaseGetAllNotesListUseCase
import com.example.notes.domain.useCases.Server.FirebaseInsertToDBUseCase
import com.example.notes.domain.useCases.Server.FirebaseUpdateDBUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): AndroidViewModel(application) {
    //Локальная база данных
    private val repository: NotesRepositoryImpl
    val allNotes: LiveData<List<NotesDomain>>
    val getAllArchivedNotes: LiveData<List<NotesDomain>>
    val getAllDeleteNotes: LiveData<List<NotesDomain>>
    private val deleteFromDBUseCase: DeleteFromDBUseCase
    private val getAllNotesListUseCase: GetAllNotesListUseCase
    private val insertToDBUseCase: InsertToDBUseCase
    private val updateDBUseCase: UpdateDBUseCase

    //Серверная база данных
    private val firebaseNotesRepository: FirebaseNotesRepositoryImpl
    val firebaseNotes: LiveData<List<NoteFirebase>>
    val getAllArchivedNotesFirebase: LiveData<List<NoteFirebase>>
    val getAllDeleteNotesFirebase: LiveData<List<NoteFirebase>>
    private val firebaseDeleteFromDBUseCase: FirebaseDeleteFromDBUseCase
    private val firebaseGetAllNotesListUseCase: FirebaseGetAllNotesListUseCase
    private val firebaseInsertToDBUseCase: FirebaseInsertToDBUseCase
    private val firebaseUpdateDBUseCase: FirebaseUpdateDBUseCase

    init {
        val dao = NotesDataBase.getDataBase(application).notesDao()
        repository = NotesRepositoryImpl(dao)
        deleteFromDBUseCase = DeleteFromDBUseCase(repository)
        getAllNotesListUseCase = GetAllNotesListUseCase(repository)
        insertToDBUseCase = InsertToDBUseCase(repository)
        updateDBUseCase = UpdateDBUseCase(repository)
        allNotes = getAllNotesListUseCase.executeNotes()
        getAllArchivedNotes = getAllNotesListUseCase.executeArchived()
        getAllDeleteNotes = getAllNotesListUseCase.executeDelete()

        // Инициализируем Firebase Repository
        firebaseNotesRepository = FirebaseNotesRepositoryImpl()
        firebaseDeleteFromDBUseCase = FirebaseDeleteFromDBUseCase(firebaseNotesRepository)
        firebaseGetAllNotesListUseCase = FirebaseGetAllNotesListUseCase(firebaseNotesRepository)
        firebaseInsertToDBUseCase = FirebaseInsertToDBUseCase(firebaseNotesRepository)
        firebaseUpdateDBUseCase = FirebaseUpdateDBUseCase(firebaseNotesRepository)
        firebaseNotes = firebaseGetAllNotesListUseCase.firebaseExecuteNotes()
        getAllArchivedNotesFirebase = firebaseGetAllNotesListUseCase.firebaseExecuteArchived()
        getAllDeleteNotesFirebase = firebaseGetAllNotesListUseCase.firebaseExecuteDelete()

    }

    //операции с локальной базой данных
    fun insertNote(note: NotesDomain) = viewModelScope.launch(Dispatchers.IO){
        insertToDBUseCase.executeInsert(note)
    }
    fun updateNote(note: NotesDomain) = viewModelScope.launch(Dispatchers.IO){
        updateDBUseCase.executeUpdate(note)
    }
    fun deleteNote(note: NotesDomain) = viewModelScope.launch(Dispatchers.IO){
        deleteFromDBUseCase.execute(note)
    }

    //операции с Firebase
    fun insertFirebase(note: NoteFirebase) = viewModelScope.launch(Dispatchers.IO) {
        firebaseInsertToDBUseCase.firebaseExecuteInsert(note)
    }

    fun updateFirebaseNote(note: NoteFirebase) = viewModelScope.launch(Dispatchers.IO) {
        firebaseUpdateDBUseCase.firebaseExecuteUpdate(note)
    }

    fun deleteFirebaseNote(note: NoteFirebase) = viewModelScope.launch(Dispatchers.IO) {
        firebaseDeleteFromDBUseCase.firebaseExecute(note)
    }

}