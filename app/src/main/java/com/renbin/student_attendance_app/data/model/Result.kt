package com.renbin.student_attendance_app.data.model

data class Result(
    val id : String = "" ,
    val name: String = "",
    val result: String = "",
    val quizId: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toHashMap(): HashMap<String, Any?> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "result" to result,
            "quizId" to quizId,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromHashMap(hash: Map<String, Any>): Result {
            return Result(
                id = hash["id"].toString(),
                name = hash["name"].toString(),
                result = hash["result"].toString(),
                quizId = hash["quizId"].toString(),
                createdAt = hash["createdAt"].toString().toLong()
            )
        }
    }
}
