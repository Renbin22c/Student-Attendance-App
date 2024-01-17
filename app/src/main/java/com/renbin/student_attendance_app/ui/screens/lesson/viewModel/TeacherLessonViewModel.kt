package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import kotlinx.coroutines.flow.StateFlow

// ViewModel interface for managing teacher-specific lesson data and actions
interface TeacherLessonViewModel {
    // StateFlow to hold the list of available classes
    val classes: StateFlow<List<String>>
    // StateFlow to hold the filtered list of lessons based on selected class
    val filterLessons: StateFlow<List<Lesson>>

    // StateFlow to hold the list of available time
    val time: StateFlow<List<String>>
    // StateFlow to hold the time that select
    val timeSelect: StateFlow<String?>
    // StateFlow to hold the class that select
    val classSelect: StateFlow<String?>

    // Function to fetch the list of available classes
    fun getClasses()
    // Function to delete a lesson by its ID
    fun deleteLesson(id: String)
    // Function to get the list of time
    fun getTime()
    // Function to update the time that select
    fun updateTimeSelect(newTime: String?)
    // Function to update the class that select
    fun updateClassSelect(newClass: String?)
}