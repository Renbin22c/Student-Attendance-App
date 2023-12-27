package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.StateFlow

interface TeacherClassesViewModel {
    val classes: StateFlow<List<Classes>>
    val teachers: StateFlow<List<Teacher>>

    fun getAllTeachers()
    fun getAllClasses()
    fun deleteClasses(id :String, name: String)
}