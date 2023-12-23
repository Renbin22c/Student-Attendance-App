package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

interface TeacherLessonViewModel {
    val lessons: StateFlow<List<Lesson>>
    val students: StateFlow<List<Student>>
    val teachers: StateFlow<List<Teacher>>
    val classes: StateFlow<List<String>>
    val dates: StateFlow<List<String>>
    val filteredLessons: StateFlow<List<Lesson>>

    fun getClassesAndDates()
    fun getAllLesson()
    fun getAllStudents()
    fun getAllTeachers()
    fun deleteLesson(id: String)
    fun filterLessons(classSelect: String?, dateSelect: String?)
}