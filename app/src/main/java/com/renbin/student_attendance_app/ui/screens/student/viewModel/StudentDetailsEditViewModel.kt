package com.renbin.student_attendance_app.ui.screens.student.viewModel

import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.StateFlow

// Interface defining the contract for the ViewModel responsible for managing student details editing
interface StudentDetailsEditViewModel {
    // StateFlow providing a list of students
    val students: StateFlow<List<Student>>
    // StateFlow providing a list of class names
    val classesName: StateFlow<List<String>>

    // Function to retrieve all class names
    fun getAllClassesName()
    // Function to retrieve all students
    fun getAllStudent()
    // Function to update a student's details
    fun updateStudent(student: Student, classes: String)
    // Function to filter students by email based on a query
    fun filterEmailByQuery(query: String): List<Student>
    // Function to delete a student by ID
    fun deleteStudent(id: String)
}
