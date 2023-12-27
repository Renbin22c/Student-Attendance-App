package com.renbin.student_attendance_app.ui.screens.student.viewModel

import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.StateFlow

interface StudentDetailsViewModel {
    val students: StateFlow<List<Student>>

    fun getAllStudents()
}