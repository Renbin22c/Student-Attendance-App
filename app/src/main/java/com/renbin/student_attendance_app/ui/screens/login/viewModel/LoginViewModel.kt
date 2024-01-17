package com.renbin.student_attendance_app.ui.screens.login.viewModel

import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

// Interface defining the contract for LoginViewModel
interface LoginViewModel {
    // StateFlow representing the current student information
    val student: StateFlow<Student>
    // StateFlow representing the current teacher information
    val teacher: StateFlow<Teacher>

    // Function to fetch and update the student information
    fun getStudent()
    // Function to fetch and update the teacher information
    fun getTeacher()
    // Function to perform the login operation with provided email and password
    fun login(email: String, pass: String)
    // Function to check is the user have been deleted or not
    fun checkUserAuthentication()
}