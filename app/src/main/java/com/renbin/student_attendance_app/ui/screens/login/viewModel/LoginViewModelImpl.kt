package com.renbin.student_attendance_app.ui.screens.login.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val authService: AuthService
): BaseViewModel(), LoginViewModel {

    override fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO){
            val result = errorHandler {
                authService.login(email, pass)
            }

            if(result!=null){
                _success.emit("Login successfully")
            }
        }
    }
}