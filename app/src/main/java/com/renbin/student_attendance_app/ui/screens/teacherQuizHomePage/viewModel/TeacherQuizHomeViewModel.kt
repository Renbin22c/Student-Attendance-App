package com.renbin.student_attendance_app.ui.screens.teacherQuizHomePage.viewModel

import com.renbin.student_attendance_app.data.model.Quiz
import kotlinx.coroutines.flow.StateFlow

interface TeacherQuizHomeViewModel {
    val quiz: StateFlow<List<Quiz>>

    fun getQuiz()

}