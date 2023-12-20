package com.renbin.student_attendance_app.core.service

import com.google.firebase.auth.FirebaseUser

interface AuthService {
    suspend fun register(email: String, pass: String): FirebaseUser?
    suspend fun login(email: String, pass: String): FirebaseUser?
    fun getCurrentUser(): FirebaseUser?
    suspend fun logout()
}