package com.renbin.student_attendance_app.ui.screens.student.viewModel

import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.StateFlow

interface StudentDetailsEditViewModel {
    val students: StateFlow<List<Student>>
    val classesName: StateFlow<List<String>>

    fun getAllClassesName()
    fun getAllStudent()
    fun updateStudent(student: Student, classes: String)
    fun filterEmailByQuery(query: String): List<Student>
    fun deleteStudent(id: String)
}