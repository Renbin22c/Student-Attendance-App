package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

interface TeacherLessonViewModel {
    val classes: StateFlow<List<String>>
    val dates: StateFlow<List<String>>
    val filteredLessons: StateFlow<List<Lesson>>

    fun getClassesAndDates()
    fun deleteLesson(id: String)
    fun filterLessons(classSelect: String?)
}