package com.example.notes.presentation.notesViewModel

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    application: Application,
    //локальная бд
    private val deleteFromDBUseCase: DeleteFromDBUseCase,
    private val getAllNotesListUseCase: GetAllNotesListUseCase,
    private val insertToDBUseCase: InsertToDBUseCase,
    private val updateDBUseCase: UpdateDBUseCase,
    //серверная бд
    private val firebaseDeleteFromDBUseCase: FirebaseDeleteFromDBUseCase,
    private val firebaseGetAllNotesListUseCase: FirebaseGetAllNotesListUseCase,
    private val firebaseInsertToDBUseCase: FirebaseInsertToDBUseCase,
    private val firebaseUpdateDBUseCase: FirebaseUpdateDBUseCase,
): AndroidViewModel(application){
    //Локальная база данных
    val allNotes: LiveData<List<NotesDomain>> = getAllNotesListUseCase.executeNotes()
    val getAllArchivedNotes: LiveData<List<NotesDomain>> = getAllNotesListUseCase.executeArchived()
    val getAllDeleteNotes: LiveData<List<NotesDomain>> = getAllNotesListUseCase.executeDelete()

    //Серверная база данных
    val firebaseNotes: LiveData<List<NoteFirebase>> = firebaseGetAllNotesListUseCase.firebaseExecuteNotes()
    val getAllArchivedNotesFirebase: LiveData<List<NoteFirebase>> = firebaseGetAllNotesListUseCase.firebaseExecuteArchived()
    val getAllDeleteNotesFirebase: LiveData<List<NoteFirebase>> = firebaseGetAllNotesListUseCase.firebaseExecuteDelete()


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
