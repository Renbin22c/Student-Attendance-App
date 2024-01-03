package com.renbin.student_attendance_app.ui.screens.note.viewModel

import com.renbin.student_attendance_app.data.model.Note
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

interface StudentNoteViewModel {
    val notes: StateFlow<List<Note>>
    fun getAllNotes()

    val teachers: StateFlow<List<Teacher>>
    fun getAllTeachers()

    val students: StateFlow<List<Student>>
    fun getAllStudents()
}