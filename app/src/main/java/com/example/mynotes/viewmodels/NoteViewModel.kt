package com.example.mynotes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mynotes.database.Note
import com.example.mynotes.database.NoteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NoteViewModel(private val noteDao: NoteDao ) : ViewModel() {
    // Get all items
    fun fullItem(): Flow<List<Note>> = noteDao.getAll()
    val allItems: LiveData<List<Note>> = noteDao.getAll().asLiveData()
    // Insert item
    private fun insertItem(note: Note){
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

    private fun getNewItemEntry(title: String, body: String):Note {
        return Note(
            noteTitle = title,
            noteBody = body)
    }

    fun addNewItem(title: String, body: String) {
        val newItem = getNewItemEntry(title, body)
        insertItem(newItem)
    }

    fun isEntryValid(title: String, body: String): Boolean {
        if (title.isBlank() || body.isBlank() ) {
            return false
        }
        return true
    }

    // udate item
    fun retrieveItem(id: Int): LiveData<Note> {
        return noteDao.getItem(id).asLiveData()
    }

    private fun updateItem(note: Note) {
        viewModelScope.launch {
            noteDao.update(note)
        }
    }


    private fun getUpdatedItemEntry(
        noteId: Int,
        title: String,
        body: String
    ):Note {
        return Note(
            id = noteId,
            noteTitle = title,
            noteBody = body

        )
    }
    fun updateItem(
        noteId: Int,
        title: String,
        body: String

    ) {
        val updatedItem = getUpdatedItemEntry(noteId, title, body)
        updateItem(updatedItem)
    }
    // delete item
    fun deleteItem(note: Note) {
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }



}