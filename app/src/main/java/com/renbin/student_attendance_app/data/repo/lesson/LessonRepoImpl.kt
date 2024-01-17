package com.renbin.student_attendance_app.data.repo.lesson

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Lesson
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LessonRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): LessonRepo {

    // Function to get the total attended classes for a specific student
    suspend fun getTotalAttendedClasses(studentId: String): Int {
        val snapshot = getDbRef().whereArrayContains("student", studentId).get().await()
        return snapshot.documents.count { document ->
            document["attendance"]?.let { attendanceList ->
                (attendanceList as List<*>).any { it as Boolean }
            } ?: false
        }
    }

    // Function to get the total absent classes for a specific student
    suspend fun getTotalAbsentClasses(studentId: String): Int {
        val snapshot = getDbRef().whereArrayContains("student", studentId).get().await()
        return snapshot.documents.count { document ->
            document["attendance"]?.let { attendanceList ->
                (attendanceList as List<*>).all { it !is Boolean || !(it as Boolean) }
            } ?: false
        }
    }

    // Function to get the reference to the FireStore collection based on the authenticated user
    private fun getDbRef(): CollectionReference {
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid?.let {
            db.collection("lessons")
        } ?: throw Exception("No authenticated user found")
    }

    // Function to get all lessons as a Flow using callbackFlow
    override suspend fun getAllLessons() = callbackFlow {
        val listener = getDbRef().addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }

            val lessons = mutableListOf<Lesson>()
            value?.documents?.let { docs ->
                for (doc in docs) {
                    doc.data?.let {
                        it["id"] = doc.id
                        lessons.add(Lesson.fromHashMap(it))
                    }
                }
                trySend(lessons)
            }
        }

        awaitClose {
            listener.remove()
        }
    }

    // Function to add a new lesson to FireStore
    override suspend fun addLesson(lesson: Lesson) {
        getDbRef().add(lesson.toHashMap()).await()
    }

    // Function to update an existing lesson by ID in FireStore
    override suspend fun updateLesson(id: String, lesson: Lesson) {
        getDbRef().document(id).set(lesson.toHashMap()).await()
    }

    // Function to delete a lesson by ID from FireStore
    override suspend fun deleteLesson(id: String) {
        getDbRef().document(id).delete().await()
    }
}