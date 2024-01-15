package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.StateFlow

// Define a ViewModel interface for managing class details
interface ClassesDetailsViewModel {
    // A StateFlow representing the list of students for a particular class
    val students: StateFlow<List<Student>>

    // Function to retrieve all students for a given class
    fun getAllStudentsByClass(classes: String)
}
