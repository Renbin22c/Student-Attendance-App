package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

// ViewModel interface for managing teacher-specific lesson data and actions
interface TeacherLessonViewModel {
    // StateFlow to hold the list of available classes
    val classes: StateFlow<List<String>>
    // StateFlow to hold the filtered list of lessons based on selected class
    val filterLessons: StateFlow<List<Lesson>>

    // Function to fetch the list of available classes
    fun getClasses()
    // Function to delete a lesson by its ID
    fun deleteLesson(id: String)
}