package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

// Define a ViewModel interface for managing teacher and class-related operations
interface TeacherClassesViewModel {

    // StateFlow representing the list of classes
    val classes: StateFlow<List<Classes>>
    // StateFlow representing the list of teachers
    val teachers: StateFlow<List<Teacher>>
    // StateFlow representing whether the list of students for a class is empty
    val isStudentsEmpty: StateFlow<Boolean>

    // Function to retrieve all teachers
    fun getAllTeachers()
    // Function to retrieve all classes
    fun getAllClasses()
    // Function to check if the list of students for a class is empty
    fun checkClassStudents(name: String)
    // Function to delete a class by its ID
    fun deleteClasses(id :String)
}
