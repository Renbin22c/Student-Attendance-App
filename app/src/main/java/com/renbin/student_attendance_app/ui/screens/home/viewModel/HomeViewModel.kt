package com.renbin.student_attendance_app.ui.screens.home.viewModel

import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

// Interface defining the contract for the HomeViewModel
interface HomeViewModel {
    // StateFlow to observe changes in the list of lessons
    val lessons: StateFlow<List<Lesson>>
    // StateFlow to observe changes in the list of students
    val students: StateFlow<List<Student>>
    // StateFlow to observe changes in the list of teachers
    val teachers: StateFlow<List<Teacher>>

    // Function to fetch and update the list of lessons
    fun getAllLesson()
    // Function to fetch and update the list of students
    fun getAllStudents()
    // Function to fetch and update the list of teachers
    fun getAllTeachers()
    // Function to handle the logout action
    fun logout()
}