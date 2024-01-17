package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.StateFlow

// Define a ViewModel interface for managing student-related operations
interface StudentClassesViewModel {

    // StateFlow representing the list of all students
    val students: StateFlow<List<Student>>
    // StateFlow representing details of a specific student
    val student: StateFlow<Student>

    // Function to retrieve details of the current user
    fun getCurrentUser()
    // Function to retrieve all students for a given class
    fun getAllStudentsByClass(classes: String)
}
