package com.renbin.student_attendance_app.data.model

data class Teacher(
    val id: String? = null,
    val name: String,
    val email: String,
    val profilePicUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
){
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "email" to email,
            "profilePicUrl" to profilePicUrl,
            "createdAt" to createdAt
        )
    }

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