package com.renbin.student_attendance_app.ui.screens.register.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.data.repo.teacher.TeacherRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// Hilt ViewModel annotation for dependency injection
@HiltViewModel
class TeacherRegisterViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val teacherRepo: TeacherRepo
): BaseViewModel(), TeacherRegisterViewModel {

    // Function to perform teacher registration with provided information
    override fun teacherRegister(name: String, email: String, pass: String, confirmPass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Use the errorHandler extension function to handle errors during registration
            val user = errorHandler { authService.register(email, pass) }
            if(user != null){
                // Use the errorHandler extension function to handle errors during teacher registration
                errorHandler {
                    teacherRepo.addTeacher(
                        Teacher(name = name, email = email)
                    )
                }
                // Emit a success message
                _success.emit("Register successfully")
            }
        }
    }
}