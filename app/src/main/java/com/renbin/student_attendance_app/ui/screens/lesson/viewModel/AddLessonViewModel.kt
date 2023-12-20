package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.StateFlow

interface AddLessonViewModel {
    val classesName: StateFlow<List<String>>
    val studentsId: StateFlow<List<Student>>
    fun getAllClassesName()
    fun getAllStudentIdByClasses(classes: String)
    fun addLesson(name: String, details: String, classes: String, date:String, time: String, studentId: List<String>)
}