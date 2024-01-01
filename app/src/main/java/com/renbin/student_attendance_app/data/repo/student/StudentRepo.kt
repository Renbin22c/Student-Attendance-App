package com.renbin.student_attendance_app.data.repo.student

import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepo {
    // Function to add a new student to the repository
    suspend fun addStudent(student: Student)

    // Function to get a single student from the repository
    suspend fun getStudent(): Student?

    // Function to get all students as a Flow
    suspend fun getAllStudents(): Flow<List<Student>>

    // Function to update an existing student in the repository
    suspend fun updateStudent(student: Student)

    // Function to get all students in a specific class
    suspend fun getAllStudentByClass(classes: String): List<Student>

    // Function to get all students in a specific class as a Flow
    suspend fun getAllStudentByClassUseFlow(classes: String): Flow<List<Student>>
}