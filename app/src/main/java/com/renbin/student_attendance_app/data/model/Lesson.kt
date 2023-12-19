package com.renbin.student_attendance_app.data.model

data class Lesson(
    val id: String? = null,
    val name: String,
    val details: String,
    val time: String,
    val date: String,
    val classes: String,
    val student: List<String>,
    val attendanceTime: List<String>,
    val attendance: List<Boolean>
) {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "details" to details,
            "date" to date,
            "classes" to classes,
            "time" to time,
            "student" to student,
            "attendanceTime" to attendanceTime,
            "attendance" to attendance
        )
    }

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
                attendance = (hash["attendance"] as List<*>).map { it as Boolean }
            )
        }
    }
}
