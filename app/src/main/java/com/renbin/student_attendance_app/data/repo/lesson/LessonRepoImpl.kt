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
    private fun getDbRef(): CollectionReference {
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid?.let{
            db.collection("lessons")
        } ?: throw Exception("No authentic user found")
    }

    override suspend fun getAllLessons()= callbackFlow {
        val listener = getDbRef().addSnapshotListener{value, error ->
            if(error != null){
                throw error
            }
            val lessons = mutableListOf<Lesson>()
            value?.documents?.let {docs ->
                for (doc in docs){
                    doc.data?.let {
                        it["id"] = doc.id
                        lessons.add(Lesson.fromHashMap(it))
                    }
                }
                trySend(lessons)
            }
        }

        awaitClose{
            listener.remove()
        }
    }

    override suspend fun addLesson(lesson: Lesson) {
        getDbRef().add(lesson.toHashMap()).await()
    }

    override suspend fun updateLesson(id: String, lesson: Lesson) {
        getDbRef().document(id).set(lesson.toHashMap()).await()
    }

    override suspend fun deleteLesson(id: String) {
        getDbRef().document(id).delete().await()
    }

}