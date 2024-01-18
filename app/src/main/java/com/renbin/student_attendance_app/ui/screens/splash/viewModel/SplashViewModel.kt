package com.renbin.student_attendance_app.ui.screens.splash.viewModel

import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

// Interface defining the contract for the SplashViewModel
interface SplashViewModel {
    // StateFlow for observing changes in the Student data
    val student: StateFlow<Student>
    // StateFlow for observing changes in the Teacher data
    val teacher: StateFlow<Teacher>

    // Function to fetch and provide Student data
    fun getStudent()
    // Function to fetch and provide Teacher data
    fun getTeacher()
    // Function to check is the user have been deleted or not
    fun checkUserAuthentication()
}