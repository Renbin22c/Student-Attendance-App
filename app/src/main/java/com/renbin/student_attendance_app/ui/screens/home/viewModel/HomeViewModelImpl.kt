package com.renbin.student_attendance_app.ui.screens.home.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val authService: AuthService
): BaseViewModel(), HomeViewModel {

    override fun logout() {
        viewModelScope.launch(Dispatchers.IO){
            errorHandler {
                authService.logout()
            }
        }
    }
}