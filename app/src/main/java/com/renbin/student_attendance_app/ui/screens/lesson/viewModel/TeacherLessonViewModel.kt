package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.StateFlow

interface TeacherLessonViewModel {
    val lessons: StateFlow<List<Lesson>>
    val students: StateFlow<List<Student>>

    fun getAllLesson()
    fun getAllStudents()
}