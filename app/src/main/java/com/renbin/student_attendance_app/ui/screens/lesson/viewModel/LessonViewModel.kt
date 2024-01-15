package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

// Interface defining the ViewModel contract for managing lessons, students, and teachers
interface LessonViewModel {
    // StateFlow representing the list of lessons
    val lessons: StateFlow<List<Lesson>>
    // StateFlow representing the list of students
    val students: StateFlow<List<Student>>
    // StateFlow representing the list of teachers
    val teachers: StateFlow<List<Teacher>>

    // Function to retrieve all lessons
    fun getAllLesson()
    // Function to retrieve all students
    fun getAllStudents()
    // Function to retrieve all teachers
    fun getAllTeachers()
}
