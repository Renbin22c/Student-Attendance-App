package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.StateFlow

interface ClassesDetailsViewModel {
    val students: StateFlow<List<Student>>

    fun getAllStudentsByClass(classes: String)
}