package com.renbin.student_attendance_app.data.repo.student

import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepo {
    suspend fun addStudent(student: Student)
    suspend fun getStudent(): Student?
    suspend fun getAllStudents(): Flow<List<Student>>
    suspend fun updateStudent(student: Student)
    suspend fun getAllStudentByClass(classes: String): Flow<List<Student>>
}