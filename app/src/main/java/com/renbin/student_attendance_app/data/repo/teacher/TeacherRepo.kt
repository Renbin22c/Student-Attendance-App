package com.renbin.student_attendance_app.data.repo.teacher

import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.flow.Flow

interface TeacherRepo {
    suspend fun addTeacher(teacher: Teacher)
    suspend fun getTeacher(): Teacher?
    suspend fun getAllTeachers(): Flow<List<Teacher>>
}