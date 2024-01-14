package com.renbin.student_attendance_app.core.service

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.renbin.student_attendance_app.core.util.Constants
import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.tasks.await

// Define AuthServiceImpl class implementing the AuthService interface
class AuthServiceImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
): AuthService {

    // Implement the register function for user registration with email and password
    override suspend fun register(email: String, pass: String): FirebaseUser? {
        // Use FirebaseAuth to create a user with the provided email and password
        val result = auth.createUserWithEmailAndPassword(email, pass).await()
        // Return the FirebaseUser obtained from the registration result
        return result.user
    }

    // Implement the login function for user login with email and password
    override suspend fun login(email: String, pass: String): FirebaseUser? {
        // Use FirebaseAuth to sign in the user with the provided email and password
        val result = auth.signInWithEmailAndPassword(email, pass).await()
        // Return the FirebaseUser obtained from the login result
        return result.user
    }

    // Implement the getCurrentUser function to get the current authenticated user
    override fun getCurrentUser(): FirebaseUser? {
        // Use FirebaseAuth to get the current user
        return auth.currentUser
    }

    // Implement the logout function for user logout
    override suspend fun logout() {
        // Use FirebaseAuth to sign out the current user
        auth.signOut()
        
    }



}
