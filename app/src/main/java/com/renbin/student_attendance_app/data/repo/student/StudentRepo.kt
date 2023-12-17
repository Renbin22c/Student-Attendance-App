package com.renbin.student_attendance_app.data.repo.student

import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.User

interface StudentRepo {
    suspend fun addStudent(student: Student)
    suspend fun getStudent(): Student?
    suspend fun updateStudent(student: Student)
}