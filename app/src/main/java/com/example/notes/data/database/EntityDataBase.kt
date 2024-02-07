package com.example.notes.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Схема таблицы Базы данных
@Entity(tableName = "notes_table")
data class EntityDataBase (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "isArchived") var isArchived: Boolean,
    @ColumnInfo(name = "isDelete") var isDelete: Boolean
): java.io.Serializable