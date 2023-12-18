package com.renbin.student_attendance_app.data.repo.lesson

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.renbin.student_attendance_app.core.service.AuthService

class LesssonRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): LesssonRepo {
    private fun getDbRef(): CollectionReference {
        return db.collection("Attendances")
    }
}