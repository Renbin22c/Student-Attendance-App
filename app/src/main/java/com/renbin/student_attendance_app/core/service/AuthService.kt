package com.renbin.student_attendance_app.core.service

import com.google.firebase.auth.FirebaseUser

// Define the AuthService interface for handling authentication-related operations
interface AuthService {
    // Function for user registration with email and password, returns FirebaseUser or null
    suspend fun register(email: String, pass: String): FirebaseUser?
    // Function for user login with email and password, returns FirebaseUser or null
    suspend fun login(email: String, pass: String): FirebaseUser?
    // Function to get the current authenticated user, returns FirebaseUser or null
    fun getCurrentUser(): FirebaseUser?
    // Function for user logout
    suspend fun logout()
}