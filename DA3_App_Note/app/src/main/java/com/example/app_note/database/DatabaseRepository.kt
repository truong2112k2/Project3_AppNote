package com.example.app_note.database

import androidx.lifecycle.LiveData

class DatabaseRepository( private val noteDAO: noteDAO) {

    val getAllNotes: LiveData<List<Note>> = noteDAO.getAllNote()

    suspend fun addNote(note: Note){
        noteDAO.InsertNote(note)
    }
    suspend fun updateNote(note: Note){
        noteDAO.UpdateNote(note)
    }
    suspend fun deleteNote(note: Note){
        noteDAO.DeleteNote(note)
    }

}