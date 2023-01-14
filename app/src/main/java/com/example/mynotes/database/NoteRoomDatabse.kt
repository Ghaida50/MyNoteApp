package com.example.mynotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Note::class) , version = 1, exportSchema = false)
abstract class NoteRoomDatabse : RoomDatabase() {

    abstract fun noteDao():NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabse? = null

        fun getDatabase(context: Context): NoteRoomDatabse {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabse::class.java,
                    "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}