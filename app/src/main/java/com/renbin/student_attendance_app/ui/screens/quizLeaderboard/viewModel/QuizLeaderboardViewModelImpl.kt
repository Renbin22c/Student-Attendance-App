package com.renbin.student_attendance_app.ui.screens.quizLeaderboard.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.data.repo.result.ResultRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import com.renbin.student_attendance_app.data.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class QuizLeaderboardViewModelImpl @Inject constructor(
    private val resultRepo: ResultRepo
) : BaseViewModel(), QuizLeaderboardViewModel {
    // A StateFlow to hold the list of leaderboard results
    private val _score: MutableStateFlow<List<Result>> = MutableStateFlow(emptyList())
    override val score: StateFlow<List<Result>> = _score

    // Initialization block called when the ViewModel is created
    init {
        // Fetch the initial set of leaderboard results
        getScore()
    }

    // Function to fetch the leaderboard results
    override fun getScore() {
        viewModelScope.launch(Dispatchers.IO) {
            // Use the errorHandler extension function to handle errors gracefully
            errorHandler {
                // Call the repository to get the list of results
                resultRepo.getResult()
            }?.collect {
                // Update the StateFlow with the fetched results
                _score.value = it
            }
        }
    }

    // Function to fetch results based on a specific quiz ID
    override fun getResultByQuizId(quizId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Use the errorHandler extension function to handle errors gracefully
            errorHandler {
                // Call the repository to get the results for a specific quiz ID
                resultRepo.getResultByQuizId(quizId)
            }?.collect {
                // Update the StateFlow with the fetched results
                _score.value = it
            }
        }
    }


}