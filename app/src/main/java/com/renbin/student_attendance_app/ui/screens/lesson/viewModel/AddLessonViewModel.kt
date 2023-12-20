package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import android.content.Context
import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.StateFlow

interface AddLessonViewModel {
    val classesName: StateFlow<List<String>>

    fun getAllClassesName()
    fun addLesson(name: String, details: String, classes: String, date:String, time: String)
    fun lessonValidation(name: String, details: String, classes: String, date: String, time: String): String?
}