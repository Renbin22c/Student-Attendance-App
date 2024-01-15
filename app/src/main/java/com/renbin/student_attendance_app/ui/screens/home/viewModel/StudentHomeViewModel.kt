package com.renbin.student_attendance_app.ui.screens.home.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import kotlinx.coroutines.flow.SharedFlow

interface StudentHomeViewModel {
    val logoutSuccess: SharedFlow<String>
    fun updateAttendanceStatus(id: String, lesson: Lesson)
}