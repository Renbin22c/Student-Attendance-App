package com.renbin.student_attendance_app.ui.screens.register.viewModel

import kotlinx.coroutines.flow.StateFlow

interface StudentRegisterViewModel {
    val classesName: StateFlow<List<String>>
    fun getAllClassesName()
}