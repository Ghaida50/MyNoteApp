package com.example.mynotes.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface  NoteDao{

    @Query("SELECT * FROM notes ")
    fun getAll(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * from notes WHERE id = :id")
    fun getItem(id: Int): Flow<Note>
}