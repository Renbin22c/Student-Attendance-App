package com.renbin.student_attendance_app.data.repo.student

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    override suspend fun getAllStudents()= callbackFlow {
        val listener = getDbRef().addSnapshotListener{ value, error ->
            if(error != null){
                throw error
            }
            val students = mutableListOf<Student>()
            value?.documents?.let {docs ->
                for(doc in docs){
                    doc.data?.let {
                        it["id"] = doc.id
                        students.add(Student.fromHashMap(it))
                    }
                }
                trySend(students)
            }
        }
        awaitClose{
            listener.remove()
        }
    }

    override suspend fun updateStudent(student: Student) {
        getDbRef().document(getUid()).set(student.toHashMap()).await()
    }

    override suspend fun getAllStudentByClass(classes: String)= callbackFlow {
        val listener = getDbRef().addSnapshotListener{value, error ->
            if(error != null){
                throw error
            }
            val students = mutableListOf<Student>()
            value?.documents?.let {docs ->
                for (doc in docs){
                    doc.data?.let {
                        if (it["classes"] == classes){
                            it["id"] = doc.id
                            students.add(Student.fromHashMap(it))
                        }
                    }
                }
                trySend(students)
            }
        }

        awaitClose{
            listener.remove()
        }
    }
}