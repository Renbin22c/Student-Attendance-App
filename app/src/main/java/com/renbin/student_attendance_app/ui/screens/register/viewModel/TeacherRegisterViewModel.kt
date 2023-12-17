package com.renbin.student_attendance_app.ui.screens.register.viewModel

interface TeacherRegisterViewModel {
    fun register(name:String, email:String, pass: String, confirmPass: String)
}