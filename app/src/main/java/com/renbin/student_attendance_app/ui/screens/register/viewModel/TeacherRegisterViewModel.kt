package com.renbin.student_attendance_app.ui.screens.register.viewModel

// Interface defining the contract for TeacherRegisterViewModel
interface TeacherRegisterViewModel {
    // Function to perform teacher registration with provided information
    fun teacherRegister(name: String, email: String, pass: String, confirmPass: String)
}