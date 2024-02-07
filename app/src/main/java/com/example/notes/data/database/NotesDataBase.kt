package com.example.notes.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notes.domain.models.DaoNotes

@Database(entities = [EntityDataBase::class], version = 2)
abstract class NotesDataBase: RoomDatabase() {
    abstract fun notesDao(): DaoNotes

    companion object{
        @Volatile
        private var INSTANCE: NotesDataBase? = null

        fun getDataBase(context: Context): NotesDataBase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDataBase::class.java,
                    "notes_table"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}