package com.example.app_note.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface noteDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun InsertNote(note: Note)
    @Update
    suspend fun UpdateNote(note: Note)
    @Delete
    suspend fun DeleteNote(note: Note)
    @Query("Select * From `notes.db`")
   fun getAllNote(): LiveData<List<Note>>
}
