package com.renbin.student_attendance_app.ui.screens.note.viewModel

import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.model.Note
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

interface TeacherNoteViewModel {
    val notes: StateFlow<List<Note>>
    val teachers: StateFlow<List<Teacher>>

    fun getAllTeachers()

    fun getAllNotes()

    fun deleteNotes(id: String)
}