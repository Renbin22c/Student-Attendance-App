package com.renbin.student_attendance_app.data.repo.lesson

import com.renbin.student_attendance_app.data.model.Lesson
import kotlinx.coroutines.flow.Flow

interface LessonRepo {
    // Function to get all lessons as a Flow
    suspend fun getAllLessons(): Flow<List<Lesson>>

    // Function to add a new lesson
    suspend fun addLesson(lesson: Lesson)

    // Function to update an existing lesson by ID
    suspend fun updateLesson(id: String, lesson: Lesson)

    // Function to delete a lesson by ID
    suspend fun deleteLesson(id: String)
}