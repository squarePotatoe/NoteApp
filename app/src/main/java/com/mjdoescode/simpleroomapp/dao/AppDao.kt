package com.mjdoescode.simpleroomapp.dao

import androidx.room.*
import com.mjdoescode.simpleroomapp.entities.NotesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    @Insert
    suspend fun insertNote(note: NotesEntity)

    @Update
    suspend fun updateNote(note: NotesEntity)

    @Query("DELETE FROM `notes` WHERE noteId = :noteId")
    suspend fun deleteNoteAtId(noteId: Int)

    @Query("SELECT * FROM `notes`")
    fun getAllNotes(): Flow<List<NotesEntity>>

    @Query("SELECT * FROM `notes` WHERE noteId = :noteId")
    fun getNoteById(noteId: Int): Flow<NotesEntity>
}