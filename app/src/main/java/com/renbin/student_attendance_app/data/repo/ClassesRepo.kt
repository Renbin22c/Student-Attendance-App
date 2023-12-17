package com.renbin.student_attendance_app.data.repo

import com.renbin.student_attendance_app.data.model.Classes
import kotlinx.coroutines.flow.Flow

interface ClassesRepo {
    suspend fun getAllClasses(): Flow<List<Classes>>
    suspend fun addClasses(classes: Classes)
    suspend fun deleteClasses(id: String)
}