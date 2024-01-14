package com.renbin.student_attendance_app.data.model

// Define a data class representing a Lesson model
data class Lesson(
    val id: String? = null,
    val name: String,
    val details: String,
    val time: String,
    val date: String,
    val classes: String,
    val student: List<String>,
    val attendanceTime: List<String>,
    val attendance: List<Boolean>,
    val createdBy: String
) {
    // Function to convert the lesson instance to a HashMap for FireStore
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "details" to details,
            "date" to date,
            "classes" to classes,
            "time" to time,
            "student" to student,
            "attendanceTime" to attendanceTime,
            "attendance" to attendance,
            "createdBy" to createdBy
        )
    }

    // Companion object containing a function to create a Lesson instance from a HashMap
    companion object {
        fun fromHashMap(hash: Map<String, Any>): Lesson {
            return Lesson(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                details = hash["details"].toString(),
                time = hash["time"].toString(),
                date = hash["date"].toString(),
                classes = hash["classes"].toString(),
                student = hash["student"] as List<String>,
                attendanceTime = hash["attendanceTime"] as List<String>,
                attendance = (hash["attendance"] as List<*>).map { it as Boolean },
                createdBy = hash["createdBy"].toString()

            )
        }
    }
}

