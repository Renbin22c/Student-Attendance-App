package com.renbin.student_attendance_app.core.service

import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class StorageService(
    private val storage: StorageReference = FirebaseStorage.getInstance().reference
) {

    suspend fun saveSelectedImageUri(name: String, uri: Uri?) {
        uri?.let {
            storage.child(name).putFile(uri).await()
        }
    }

    suspend fun loadSelectedImageUri(name: String): Uri? {
        return try {
            storage.child(name).downloadUrl.await()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getCurrentUserId(): String {
        val currentUser = Firebase.auth.currentUser
        var currentUserId = ""
        if (currentUser != null)
            currentUserId = currentUser.uid
        return currentUserId
    }
}
