package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

interface StudentLessonViewModel {
    val lessons: StateFlow<List<Lesson>>
    val students: StateFlow<List<Student>>
    val teachers: StateFlow<List<Teacher>>

    fun getAllLesson()
    fun getAllStudents()
    fun getAllTeachers()
    fun updateAttendanceStatus(id: String, lesson: Lesson)
}