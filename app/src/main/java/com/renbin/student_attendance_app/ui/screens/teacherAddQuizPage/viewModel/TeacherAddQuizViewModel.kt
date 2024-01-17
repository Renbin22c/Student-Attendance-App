package com.renbin.student_attendance_app.ui.screens.teacherAddQuizPage.viewModel

import kotlinx.coroutines.flow.SharedFlow

interface TeacherAddQuizViewModel {

    val finish: SharedFlow<Unit>

    fun addQuiz(id: String, title: String, time: Long)

    fun readCsv(lines:List<String>)
}