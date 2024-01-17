package com.renbin.student_attendance_app.ui.screens.teacherQuizHomePage.viewModel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Quiz
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.data.repo.quiz.QuizRepo
import com.renbin.student_attendance_app.data.repo.teacher.TeacherRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherQuizHomeViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo,
    private val authService: AuthService,
    private val teacherRepo: TeacherRepo
): BaseViewModel(), TeacherQuizHomeViewModel {

    private val _finish = MutableSharedFlow<Unit>()
    val finish : SharedFlow<Unit> = _finish

    private val _quiz = MutableStateFlow(
        (1..2).map {
            Quiz(title = "title $it", quizId = "quiz ID $it", titles = emptyList(), options = emptyList(), answers = emptyList(), createdBy = "", timer = 0)
        }.toList()
    )

    override val quiz: StateFlow<List<Quiz>> = _quiz

    init {
        getQuiz()
        getCurrentUser()
    }

    override fun getQuiz() {
        viewModelScope.launch(Dispatchers.IO) {
            quizRepo.getQuizs().collect {
                _quiz.value = it
            }
        }

    }


    fun getCurrentUser() {
        val firebaseUser = authService.getCurrentUser()
        firebaseUser?.let{
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler { teacherRepo.getTeacher() }?.let{user ->

                }
            }
        }
    }

    fun delete(quiz: Quiz){
        viewModelScope.launch(Dispatchers.IO) {
            quizRepo.deleteQuiz(quiz.id)
        }
    }

}