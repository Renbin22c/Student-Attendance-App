package com.renbin.student_attendance_app.ui.screens.login.viewModel

import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

interface LoginViewModel {
    val student: StateFlow<Student>
    val teacher: StateFlow<Teacher>
    fun getStudent()
    fun getTeacher()
    fun login(email: String, pass: String)
}