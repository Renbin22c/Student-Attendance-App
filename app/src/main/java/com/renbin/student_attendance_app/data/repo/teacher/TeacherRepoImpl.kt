package com.renbin.student_attendance_app.data.repo.teacher

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Teacher
import kotlinx.coroutines.tasks.await

class TeacherRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): TeacherRepo {
    private fun getDbRef(): CollectionReference {
        return db.collection("teachers")
    }

    private fun getUid(): String{
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid ?: throw Exception("No authentication user found")
    }

    override suspend fun addTeacher(teacher: Teacher) {
        getDbRef().document(getUid()).set(teacher.toHashMap()).await()
    }

    override suspend fun getTeacher(): Teacher? {
        val doc =  getDbRef().document(getUid()).get().await()
        return doc.data?.let {
            it["id"] = getUid()
            Teacher.fromHashMap(it)
        }
    }
}