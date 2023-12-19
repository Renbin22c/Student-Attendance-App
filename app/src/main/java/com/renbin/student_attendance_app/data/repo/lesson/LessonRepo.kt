package com.renbin.student_attendance_app.data.repo.lesson

import com.renbin.student_attendance_app.data.model.Lesson
import kotlinx.coroutines.flow.Flow

interface LessonRepo {
    suspend fun getAllLessons(): Flow<List<Lesson>>
    suspend fun addLesson(lesson: Lesson)
    suspend fun deleteLesson(id:String)
}