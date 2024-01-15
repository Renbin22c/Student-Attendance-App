package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

// Interface defining the ViewModel contract for managing student attendance in lessons
interface StudentLessonViewModel {
    // Function to update the attendance status of a student in a lesson
    fun updateAttendanceStatus(id: String, lesson: Lesson)
}