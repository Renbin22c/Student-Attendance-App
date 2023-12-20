package com.renbin.student_attendance_app.core.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthServiceImpl(
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
): AuthService {
    override suspend fun register(email: String, pass: String): FirebaseUser? {
        val result = auth.createUserWithEmailAndPassword(email, pass).await()
        return result.user
    }

    override suspend fun login(email: String, pass: String): FirebaseUser? {
        val result = auth.signInWithEmailAndPassword(email, pass).await()
        return result.user
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun logout() {
        auth.signOut()
    }
}