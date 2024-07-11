package com.example.app_note.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.app_note.database.DatabaseNote
import com.example.app_note.database.DatabaseRepository
import com.example.app_note.database.Note
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class viewModel( application: Application): AndroidViewModel(application) {

   private val noteDao = DatabaseNote.getDatabase(application).noteDAO()
   private val noteRepository = DatabaseRepository(noteDao)
    val getAllNote = noteRepository.getAllNotes // return all note on database

    fun addNote(note: Note){
        viewModelScope.launch(Dispatchers.IO)
        {
            noteRepository.addNote(note)
        }
    }

    fun updateNote(note: Note){
        viewModelScope.launch(Dispatchers.IO)
        {
            noteRepository.updateNote(note)
        }
    }


    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO)
        {
            noteRepository.deleteNote(note)
        }
    }




    private var _title = MutableLiveData<String>()
    val title : LiveData<String> get() = _title

    private var _content = MutableLiveData<String>()
    val content : LiveData<String> get() = _content

    private var _date = MutableLiveData<String>()
    val date : LiveData<String> get() = _date

    private var _note = MutableLiveData<Note>()
    val note: LiveData<Note> get() = _note

    fun addNote(edtTitle: EditText, edtContent: EditText, date: String, date_notification: String, time_notification: String ){ // for add a new note
        _title.value = edtTitle.text.toString()
        _content.value = edtContent.text.toString()
        _date.value = date
        val newNote = Note(title = _title.value.toString(), content = _content.value.toString(), date = _date.value.toString(), date_notification = date_notification, time_notification = time_notification)
        _note.value = newNote
    }


    fun updateNote(id: String, edtTitle: EditText, edtContent: EditText, date: String, date_notification: String, time_notification: String ){ // for update
        _title.value = edtTitle.text.toString()
        _content.value = edtContent.text.toString()
        _date.value = date
        val newNote = Note(id = id.toInt(), title = _title.value.toString(), content = _content.value.toString(), date = _date.value.toString(),date_notification = date_notification, time_notification = time_notification)
        _note.value = newNote
    }

}