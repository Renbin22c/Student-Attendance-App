package com.renbin.student_attendance_app.data.repo.classes

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Classes
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ClassesRepoImpl(
    private val authService: AuthService,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
): ClassesRepo {

    // Function to get the reference to the FireStore collection based on the authenticated user
    private fun getDbRef(): CollectionReference {
        val firebaseUser = authService.getCurrentUser()
        return firebaseUser?.uid?.let {
            db.collection("classes")
        } ?: throw Exception("No authenticated user found")
    }

    // Function to get all classes as a Flow using callbackFlow
    override suspend fun getAllClasses() = callbackFlow {
        val listener = getDbRef().addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }

            val classes = mutableListOf<Classes>()
            value?.documents?.let { docs ->
                for (doc in docs) {
                    doc.data?.let {
                        it["id"] = doc.id
                        classes.add(Classes.fromHashMap(it))
                    }
                }
                trySend(classes)
            }
        }

        awaitClose {
            listener.remove()
        }
    }

    // Function to get all class names as a Flow using callbackFlow
    override suspend fun getAllClassesName() = callbackFlow {
        val listener = db.collection("classes").addSnapshotListener { value, error ->
            if (error != null) {
                throw error
            }

            val classNames = mutableListOf<String>()
            value?.documents?.let { docs ->
                for (doc in docs) {
                    doc.data?.let {
                        it["name"]?.let { className ->
                            classNames.add(className.toString())
                        }
                    }
                }
                trySend(classNames)
            }
        }

        awaitClose {
            listener.remove()
        }
    }

    // Function to add a new class to FireStore
    override suspend fun addClasses(classes: Classes) {
        getDbRef().add(classes.toHashMap()).await()
    }

    // Function to delete a class by ID from FireStore
    override suspend fun deleteClasses(id: String) {
        getDbRef().document(id).delete().await()
    }
}