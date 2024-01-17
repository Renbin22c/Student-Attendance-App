package com.renbin.student_attendance_app.data.repo.quiz

import com.renbin.student_attendance_app.data.model.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepo {

    suspend fun getQuizs(): Flow<List<Quiz>>

    suspend fun addQuiz(quiz: Quiz)

    suspend fun getQuizById(quizId: String): Quiz?


    suspend fun deleteQuiz(id: String)




}