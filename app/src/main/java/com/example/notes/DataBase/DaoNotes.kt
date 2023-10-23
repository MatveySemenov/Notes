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

    //Вывод в порядке возрастания по дате создания
    @Query("SELECT * from notes_table ORDER BY date ASC")
    fun getAllNotes(): LiveData<List<EntityDataBase>>

    //Обновление таблицы
    @Query("UPDATE notes_table set title= :title, note = :note, date = :date where id = :id")
    suspend fun update(id: Int?, title: String?, note: String?,date: String)

}