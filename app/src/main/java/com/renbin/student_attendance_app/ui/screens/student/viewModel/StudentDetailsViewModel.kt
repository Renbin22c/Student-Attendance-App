package com.renbin.student_attendance_app.ui.screens.student.viewModel

import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.StateFlow

// ViewModel interface for managing student details.
interface StudentDetailsViewModel {
    // StateFlow representing the list of students
    val students: StateFlow<List<Student>>

    // Function to retrieve all students.
    fun getAllStudents()
}
