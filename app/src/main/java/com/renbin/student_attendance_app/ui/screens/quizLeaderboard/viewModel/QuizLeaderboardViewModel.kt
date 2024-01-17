package com.renbin.student_attendance_app.ui.screens.quizLeaderboard.viewModel

import kotlinx.coroutines.flow.StateFlow
import com.renbin.student_attendance_app.data.model.Result

interface QuizLeaderboardViewModel {
    val score: StateFlow<List<Result>>

    fun getScore()
    fun getResultByQuizId(quizId: String)
}