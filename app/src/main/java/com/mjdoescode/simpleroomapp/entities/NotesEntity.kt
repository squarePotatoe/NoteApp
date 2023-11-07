package com.mjdoescode.simpleroomapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
class NotesEntity (

    var noteContent: String,
    var noteReminderTime: String,
    val noteTimeStamp: String,
    @PrimaryKey(autoGenerate = true)
    val noteId: Int = 0
) {
}