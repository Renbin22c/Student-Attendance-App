package com.renbin.student_attendance_app.data.repo.notes

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Note
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NoteRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): NoteRepo {

    private fun getDbRef(): CollectionReference {
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid?.let {
            db.collection("notes")
        } ?: throw Exception("No authentic user found")
    }

    override suspend fun getAllNotes()= callbackFlow {
        val listener = getDbRef().addSnapshotListener { value, error ->
            if(error != null) {
                throw error
            }
            val notes = mutableListOf<Note>()
            value?.documents?.let { docs ->
                for(doc in docs) {
                    doc.data?.let {
                        it["id"] = doc.id
                        notes.add(Note.fromHashMap(it))
                    }
                }
                trySend(notes)
            }
        }
        awaitClose {
            listener.remove()
        }
    }

    override suspend fun addNote(note: Note) {
        getDbRef().add(note.toHashMap()).await()
    }

    override suspend fun getNote(id: String): Note? {
        val doc = getDbRef().document(id).get().await()
        return doc.data?.let {
            it["id"] = doc.id
            Note.fromHashMap(it)
        }
    }

    override suspend fun updateNote(id: String, note: Note) {
        getDbRef().document(id).set(note.toHashMap()).await()
    }

    override suspend fun deleteNote(id: String) {
        getDbRef().document(id).delete()
    }
}