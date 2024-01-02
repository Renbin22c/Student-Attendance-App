package com.renbin.student_attendance_app.ui.screens.profile.viewModel

import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.StateFlow

interface StudentProfileViewModel {
    val user: StateFlow<Student>

    fun getCurrentUser()
}