package com.renbin.student_attendance_app.ui.screens.studentJoinQuizPage.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.data.model.Quiz
import com.renbin.student_attendance_app.data.repo.quiz.QuizRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentJoinQuizPageViewModelImpl @Inject constructor(
    private val quizRepo: QuizRepo
) : BaseViewModel(), StudentJoinQuizPageViewModel {

    override fun checkIfQuizExists(id: String, callback: StudentJoinQuizPageViewModel.QuizExistsCallback) {
        viewModelScope.launch {
            try {
                val quiz: Quiz? = quizRepo.getQuizById(id)

                if (quiz != null) {
                    callback.onQuizExists()
                } else {
                    callback.onQuizDoesNotExist()
                }

            } catch (e: Exception) {
                callback.onQuizDoesNotExist()
            }
        }
    }
}
