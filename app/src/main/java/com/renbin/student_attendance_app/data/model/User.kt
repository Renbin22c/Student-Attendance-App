package com.renbin.student_attendance_app.data.model
data class User(
    val id: String? = null,
    val name: String,
    val email: String,
    val role: String,
    val classes: String = "",
    val profilePicUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
){
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "email" to email,
            "role" to role,
            "classes" to classes,
            "profilePicUrl" to profilePicUrl,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromHashMap(hash: Map<String, Any>): User{
            return User(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                email = hash["email"].toString(),
                role = hash["role"].toString(),
                classes = hash["classes"].toString(),
                profilePicUrl = hash["profilePicUrl"].toString(),
                createdAt = hash["createdAt"].toString().toLong()
            )
        }
    }
}