package com.renbin.student_attendance_app.data.model

data class Quiz(
    val quizId: String = "",
    val id: String ="",
    val title: String = "",
    val titles: List<String>,
    val options: List<String>,
    val answers: List<String>,
    val createdBy: String = "",
    val timer: Long,
    val csv: String = ""
) {
    fun toHashMap():HashMap<String, Any> {
        return hashMapOf(
            "quizId" to quizId,
            "id" to id,
            "title" to title,
            "titles" to titles,
            "options" to options,
            "answers" to answers,
            "createdBy" to createdBy,
            "timer" to timer,
            "csv" to csv
        )
    }


    companion object {

        fun fromHashMap(hash: Map<String, Any>): Quiz {
            return Quiz(
                quizId = hash["quizId"].toString(),
                id = hash["id"].toString(),
                title = hash["title"].toString(),
                titles = (hash["titles"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                options = (hash["options"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                answers = (hash["answers"] as ArrayList<*>?)?.map {
                    it.toString()
                }?.toList() ?: emptyList(),
                createdBy = hash["createdBy"].toString(),
                timer = hash["timer"]?.toString()?.toLong() ?: -1,
                csv = hash["csv"].toString()
            )
        }
    }
}