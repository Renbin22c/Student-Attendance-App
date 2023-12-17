package com.renbin.student_attendance_app.data.model

data class Classes(
    val id: String? = null,
    val name: String,
    val teacher: String,
){
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "name" to name,
            "teacher" to teacher,

        )
    }

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
