package com.renbin.student_attendance_app.data.model

// Define a data class representing a Classes model
data class Classes(
    val id: String? = null,
    val name: String,
    val teacher: String,
){
    // Function to convert the class instance to a HashMap for FireStore
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "teacher" to teacher
        )
    }

    // Companion object containing a function to create a Classes instance from a HashMap
    companion object {
        fun fromHashMap(hash: Map<String, Any>): Classes{
            return Classes(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                teacher = hash["teacher"].toString()
            )
        }
    }
}
