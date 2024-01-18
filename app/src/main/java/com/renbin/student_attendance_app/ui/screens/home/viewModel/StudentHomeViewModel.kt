package com.renbin.student_attendance_app.ui.screens.home.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import kotlinx.coroutines.flow.SharedFlow

// Interface defining the contract for the StudentHomeViewModel
interface StudentHomeViewModel {
    // SharedFlow to emit a logout success event
    val logoutSuccess: SharedFlow<String>

    // Function to update the attendance status for a lesson
    fun updateAttendanceStatus(id: String, lesson: Lesson)
}