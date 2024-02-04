package com.example.notes.DataBase

import androidx.lifecycle.LiveData
import androidx.room.*

//Создание методов для взаимодействия с базой данных
@Dao
interface DaoNotes {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: EntityDataBase)

    @Delete
    suspend fun delete(note: EntityDataBase)

    //Вывод в порядке возрастания по дате создания не архивированных
    @Query("SELECT * from notes_table WHERE isArchived = 0 ORDER BY date ASC")
    fun getAllNotes(): LiveData<List<EntityDataBase>>

    //Вывод в порядке возрастания по дате создания архивированных
    @Query("SELECT * from notes_table WHERE isArchived = 1 ORDER BY date ASC")
    fun getAllArchivedNotes(): LiveData<List<EntityDataBase>>

    //Обновление таблицы
    @Query("UPDATE notes_table set title= :title, note = :note, date = :date,isArchived = :isArchived  where id = :id")
    suspend fun update(id: Int?, title: String?, note: String?,date: String,isArchived: Boolean)

}