package com.renbin.student_attendance_app.data.repo.student

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.tasks.await

class StudentRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): StudentRepo {
    private fun getDbRef(): CollectionReference {
        return db.collection("students")
    }

    private fun getUid(): String{
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid ?: throw Exception("No authentication user found")
    }

    override suspend fun addStudent(student: Student) {
        getDbRef().document(getUid()).set(student.toHashMap()).await()
    }

    override suspend fun getStudent(): Student? {
        val doc =  getDbRef().document(getUid()).get().await()
        return doc.data?.let {
            it["id"] = getUid()
            Student.fromHashMap(it)
        }
    }

    override suspend fun updateStudent(student: Student) {
        getDbRef().document(getUid()).set(student.toHashMap()).await()
    }
}