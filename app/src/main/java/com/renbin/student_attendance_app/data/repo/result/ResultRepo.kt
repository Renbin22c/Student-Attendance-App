package com.renbin.student_attendance_app.data.repo.result

import kotlinx.coroutines.flow.Flow
import com.renbin.student_attendance_app.data.model.Result

interface ResultRepo {
    suspend fun addResult(result: Result)
    suspend fun getResult() : Flow<List<Result>>
    suspend fun getResultByQuizId(quizId: String): Flow<List<Result>>
}