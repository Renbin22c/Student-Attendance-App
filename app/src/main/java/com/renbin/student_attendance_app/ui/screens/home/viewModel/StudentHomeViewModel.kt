package com.renbin.student_attendance_app.ui.screens.home.viewModel

import com.renbin.student_attendance_app.data.model.Lesson

interface StudentHomeViewModel {
    fun updateAttendanceStatus(id: String, lesson: Lesson)
}