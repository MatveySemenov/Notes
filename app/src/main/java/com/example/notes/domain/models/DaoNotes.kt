package com.example.notes.domain.models

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notes.data.database.EntityDataBase

//Создание методов для взаимодействия с базой данных
@Dao
interface DaoNotes {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: EntityDataBase)

    @Delete
    suspend fun delete(note: EntityDataBase)

    //Вывод в порядке возрастания по дате создания не архивированных
    @Query("SELECT * from notes_table WHERE isArchived = 0 AND isDelete = 0 ORDER BY date ASC")
    fun getAllNotes(): LiveData<List<EntityDataBase>>

    //Вывод в порядке возрастания по дате создания архивированных
    @Query("SELECT * from notes_table WHERE isArchived = 1 AND isDelete = 0 ORDER BY date ASC")
    fun getAllArchivedNotes(): LiveData<List<EntityDataBase>>

    //Вывод в порядке возрастания по дате создания удаленных
    @Query("SELECT * from notes_table WHERE isDelete = 1 ORDER BY date ASC")
    fun getAllDeleteNotes(): LiveData<List<EntityDataBase>>

    //Обновление таблицы
    @Query("UPDATE notes_table set title= :title, note = :note, date = :date,isArchived = :isArchived, isDelete = :isDelete  where id = :id")
    suspend fun update(id: Int?, title: String?, note: String?,date: String,isArchived: Boolean,isDelete: Boolean)

}