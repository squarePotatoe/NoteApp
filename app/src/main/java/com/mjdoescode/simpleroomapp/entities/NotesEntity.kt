package com.mjdoescode.simpleroomapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
class NotesEntity(
    @PrimaryKey(autoGenerate = true)
    val noteId: Int = 0,
    var noteContent: String,
    var noteReminderTime: String,
    val noteTimeStamp: String
) {
}