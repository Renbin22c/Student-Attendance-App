package com.renbin.student_attendance_app.ui.screens.studentJoinQuizPage.viewModel

interface StudentJoinQuizPageViewModel {
    fun checkIfQuizExists(id: String, callback: QuizExistsCallback)

    interface QuizExistsCallback {
        fun onQuizExists()
        fun onQuizDoesNotExist()
    }
}