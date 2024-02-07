package com.example.notes.di

import android.content.Context
import com.example.notes.data.FirebaseNotesRepositoryImpl
import com.example.notes.data.NotesRepositoryImpl
import com.example.notes.data.database.DaoNotes
import com.example.notes.data.database.NotesDataBase
import com.example.notes.domain.FirebaseNotesRepository
import com.example.notes.domain.NotesRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Singleton
    @Provides
    fun provideNotesRepository(daoNotes: DaoNotes): NotesRepository{
        return NotesRepositoryImpl(daoNotes)
    }

    @Singleton
    @Provides
    fun provideDao(dataBase: NotesDataBase): DaoNotes{
        return dataBase.notesDao()
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context):NotesDataBase{
        return NotesDataBase.getDataBase(context)
    }

    @Singleton
    @Provides
    fun provideFirebaseRepository(): FirebaseNotesRepository{
        return FirebaseNotesRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideFirebase(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }
}