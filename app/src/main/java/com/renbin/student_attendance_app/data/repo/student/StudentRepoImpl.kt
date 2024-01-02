package com.renbin.student_attendance_app.data.repo.student

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Student
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class StudentRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): StudentRepo {
    // Get the reference to the FireStore collection for students
    private fun getDbRef(): CollectionReference {
        return db.collection("students")
    }

    // Get the UID of the current authenticated user
    private fun getUid(): String {
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid ?: throw Exception("No authenticated user found")
    }

    // Function to add a new student to the repository
    override suspend fun addStudent(student: Student) {
        getDbRef().document(getUid()).set(student.toHashMap()).await()
    }

    // Function to get a single student from the repository
    override suspend fun getStudent(): Student? {
        val doc = getDbRef().document(getUid()).get().await()
        return doc.data?.let {
            it["id"] = getUid()
            Student.fromHashMap(it)
        }
    }

    // Function to get all students as a Flow
    override suspend fun getAllStudents() = callbackFlow {
        val listener = getDbRef().addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val students = mutableListOf<Student>()
            value?.documents?.let { docs ->
                for (doc in docs) {
                    doc.data?.let {
                        it["id"] = doc.id
                        students.add(Student.fromHashMap(it))
                    }
                }
                trySend(students)
            }
        }
        awaitClose {
            listener.remove()
        }
    }

    // Function to update an existing student in the repository
    override suspend fun updateStudent(student: Student) {
        if (student.id != null) {
            getDbRef().document(student.id).set(student.toHashMap()).await()
        }
    }

    // Function to get all students in a specific class
    override suspend fun getAllStudentByClass(classes: String): List<Student> {
        return try {
            val querySnapshot = getDbRef()
                .whereEqualTo("classes", classes)
                .get()
                .await()

            val students = mutableListOf<Student>()

            for (document in querySnapshot.documents) {
                document.data?.let {
                    it["id"] = document.id
                    students.add(Student.fromHashMap(it))
                }
            }

            students
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Function to get all students in a specific class as a Flow
    override suspend fun getAllStudentByClassUseFlow(classes: String) = callbackFlow {
        val listener = getDbRef().addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }
            val students = mutableListOf<Student>()
            value?.documents?.let { docs ->
                for (doc in docs) {
                    doc.data?.let {
                        if (it["classes"] == classes) {
                            it["id"] = doc.id
                            students.add(Student.fromHashMap(it))
                        }
                    }
                }
                trySend(students)
            }
        }

        awaitClose {
            listener.remove()
        }
    }
}