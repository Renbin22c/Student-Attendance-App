package com.renbin.student_attendance_app.ui.screens.splash.viewModel

import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

interface SplashViewModel {
    val student: StateFlow<Student>
    val teacher: StateFlow<Teacher>
    fun getStudent()
    fun getTeacher()
}