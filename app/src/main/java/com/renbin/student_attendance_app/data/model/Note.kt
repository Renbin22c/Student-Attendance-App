package com.renbin.student_attendance_app.data.model

data class Note(
    val id: String? = null,
    val title: String,
    val desc: String,
    val classes: String,
    val student: List<String>,
    val createdBy: String
) {
    fun toHashMap(): Map<String, Any> {
        return mapOf(
            "title" to title,
            "desc" to desc,
            "classes" to classes,
            "student" to student,
            "createdBy" to createdBy
        )
    }
    companion object {
        fun fromHashMap(hash: Map<String, Any>):Note {
            return Note(
                id = hash["id"].toString(),
                title = hash["title"].toString(),
                desc = hash["desc"].toString(),
                classes = hash["classes"].toString(),
                student = (hash["student"] as? List<String>) ?: emptyList(),
                createdBy = hash["createdBy"].toString()
            )
        }
    }
}
