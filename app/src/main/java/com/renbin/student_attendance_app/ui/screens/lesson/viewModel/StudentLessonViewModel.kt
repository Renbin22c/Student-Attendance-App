package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

interface StudentLessonViewModel {
    fun updateAttendanceStatus(id: String, lesson: Lesson)
}