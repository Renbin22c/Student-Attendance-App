package com.renbin.student_attendance_app.ui.screens.register.viewModel

interface TeacherRegisterViewModel {
    fun teacherRegister(name:String, email:String, pass: String, confirmPass: String)
}