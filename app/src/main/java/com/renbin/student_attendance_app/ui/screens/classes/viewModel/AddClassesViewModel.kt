package com.renbin.student_attendance_app.ui.screens.classes.viewModel

interface AddClassesViewModel {
    fun addClasses(name: String)

    fun validation(name: String) :String?
}