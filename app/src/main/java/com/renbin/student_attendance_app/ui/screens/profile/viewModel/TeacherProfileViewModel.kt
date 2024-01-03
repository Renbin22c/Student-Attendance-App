package com.renbin.student_attendance_app.ui.screens.profile.viewModel

import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface TeacherProfileViewModel {
    val user: MutableStateFlow<Teacher>

    fun getCurrentUser()
}