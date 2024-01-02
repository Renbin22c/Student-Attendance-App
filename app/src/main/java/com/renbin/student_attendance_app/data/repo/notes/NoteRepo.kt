package com.renbin.student_attendance_app.data.repo.notes

import com.renbin.student_attendance_app.data.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepo {
    suspend fun getAllNotes(): Flow<List<Note>>
    suspend fun addNote(note: Note)
    suspend fun getNote(id: String): Note?

    suspend fun updateNote(id: String, note: Note)
    suspend fun deleteNote(id: String)
}