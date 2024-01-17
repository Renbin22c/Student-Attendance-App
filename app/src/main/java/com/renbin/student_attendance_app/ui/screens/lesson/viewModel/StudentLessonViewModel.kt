package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import kotlinx.coroutines.flow.StateFlow

// Interface defining the ViewModel contract for managing student attendance in lessons
interface StudentLessonViewModel {

    // StateFlow to hold the list of available time
    val time: StateFlow<List<String>>
    // StateFlow to hold the time that select
    val timeSelect: StateFlow<String?>

    // Function to update the attendance status of a student in a lesson
    fun updateAttendanceStatus(id: String, lesson: Lesson)
    // Function to get the list of time
    fun getTime()
    // Function to update the time that select
    fun updateTimeSelect(newTime: String?)
}