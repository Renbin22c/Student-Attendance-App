package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

// Interface defining the contract for AddLessonViewModel
interface AddLessonViewModel {
    // StateFlow representing the list of available classes' names
    val classesName: StateFlow<List<String>>

    // Function to fetch and provide a list of all available classes' names
    fun getAllClassesName()
    // Function to add a new lesson with provided information
    fun addLesson(name: String, details: String, classes: String, date: String, time: String, context: Context)
    // Function to perform validation on lesson information and return an error message if validation fails
    fun lessonValidation(name: String, details: String, classes: String, date: String, time: String): String?
}