package com.renbin.student_attendance_app.data.repo.teacher

import com.renbin.student_attendance_app.data.model.Teacher

interface TeacherRepo {
    suspend fun addTeacher(teacher: Teacher)
    suspend fun getTeacher(): Teacher?
}