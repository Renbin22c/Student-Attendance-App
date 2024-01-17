package com.renbin.student_attendance_app.ui.screens.quizPage.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Quiz
import com.renbin.student_attendance_app.data.model.Result
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.repo.quiz.QuizRepo
import com.renbin.student_attendance_app.data.repo.result.ResultRepo
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizPageViewModelImpl  @Inject constructor(
    private val quizRepo: QuizRepo,
    private val authService: AuthService,
    private val studentRepo: StudentRepo,
    private val resultRepo: ResultRepo
) : BaseViewModel(), QuizPageViewModel{
    private val _user = MutableStateFlow(Student("id", "Name", "Email", "ProfileUrl", "Role"))
    val user: StateFlow<Student> = _user

    private val _remainingTime = MutableStateFlow<String?>(null)
    val remainingTime: StateFlow<String?> = _remainingTime

    private val _quiz = MutableStateFlow(
        Quiz(
            titles = emptyList(),
            options = emptyList(),
            answers = emptyList(),
            createdBy = "",
            quizId = "",
            title = "",
            timer = -1
        )
    )
    val quiz: StateFlow<Quiz> = _quiz

    init {
        getCurrentUser()
    }

    override fun getQuiz(quizId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _quiz.value = errorHandler {
                quizRepo.getQuizById(quizId)
            } ?: _quiz.value // If null, keep the existing value
        }
    }

    fun startCountdownTimer(timeLimit: Long) {
        object : CountDownTimer(timeLimit * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / (60 * 1000)
                val seconds = (millisUntilFinished % (60 * 1000)) / 1000
                _remainingTime.value = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {}
        }.start()
    }

    private fun getCurrentUser() {
        authService.getCurrentUser()?.let { firebaseUser ->
            viewModelScope.launch(Dispatchers.IO) {
                _user.value = errorHandler {
                    studentRepo.getUser(firebaseUser.uid)
                } ?: _user.value // If null, keep the existing value
            }
        }
    }

    fun addResult(result: String, quizId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                resultRepo.addResult(Result(result=result, name = user.value.name, quizId = quizId))
            }
        }
    }

}