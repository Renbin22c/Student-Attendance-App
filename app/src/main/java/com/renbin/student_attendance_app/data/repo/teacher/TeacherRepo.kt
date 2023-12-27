package com.renbin.student_attendance_app.data.repo.teacher

import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.Flow

interface TeacherRepo {
    // Function to add a new teacher to the repository
    suspend fun addTeacher(teacher: Teacher)

    // Function to get a single teacher from the repository
    suspend fun getTeacher(): Teacher?

    // Function to get all teachers as a Flow
    suspend fun getAllTeachers(): Flow<List<Teacher>>
}