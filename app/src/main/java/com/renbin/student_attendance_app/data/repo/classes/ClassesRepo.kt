package com.renbin.student_attendance_app.data.repo.classes

import com.renbin.student_attendance_app.data.model.Classes
import kotlinx.coroutines.flow.Flow

interface ClassesRepo {
    // Function to get all classes as a Flow
    suspend fun getAllClasses(): Flow<List<Classes>>

    // Function to get all class names as a Flow
    suspend fun getAllClassesName(): Flow<List<String>>

    // Function to add a new class
    suspend fun addClasses(classes: Classes)

    // Function to delete a class by ID
    suspend fun deleteClasses(id: String)
}