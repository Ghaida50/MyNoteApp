package com.example.mynotes

import android.app.Application
import com.example.mynotes.database.NoteRoomDatabse

class NoteApplication : Application(){
    val database: NoteRoomDatabse by lazy {
        NoteRoomDatabse.getDatabase(this)
    }
}