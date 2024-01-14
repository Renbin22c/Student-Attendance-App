package com.renbin.student_attendance_app.data.model

// Define a data class representing a Teacher model
data class Teacher(
    val id: String? = null,
    var name: String,
    val email: String,
    var profilePicUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
){
    // Function to convert the teacher instance to a HashMap for FireStore
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "email" to email,
            "profilePicUrl" to profilePicUrl,
            "createdAt" to createdAt
        )
    }

    // Companion object containing a function to create a Teacher instance from a HashMap
    companion object {
        fun fromHashMap(hash: Map<String, Any>): Teacher{
            return Teacher(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                email = hash["email"].toString(),
                profilePicUrl = hash["profilePicUrl"].toString(),
                createdAt = hash["createdAt"].toString().toLong()
            )
        }
    }
}
