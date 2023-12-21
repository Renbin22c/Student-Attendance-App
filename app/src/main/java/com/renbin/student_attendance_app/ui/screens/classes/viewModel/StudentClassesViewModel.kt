package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.StateFlow

interface StudentClassesViewModel {
    val students: StateFlow<List<Student>>
    val student: StateFlow<Student>

    fun getCurrentUser()
    fun getAllStudentsByClass(classes: String)
}