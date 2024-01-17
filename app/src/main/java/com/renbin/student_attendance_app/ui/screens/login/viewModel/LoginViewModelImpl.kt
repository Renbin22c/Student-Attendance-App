package com.renbin.student_attendance_app.ui.screens.login.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.data.repo.teacher.TeacherRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
// Hilt ViewModel annotation for dependency injection
@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val teacherRepo: TeacherRepo,
    private val studentRepo: StudentRepo
): BaseViewModel(), LoginViewModel {

    // MutableStateFlow for holding teacher information with initial values
    private val _teacher = MutableStateFlow(Teacher(name = "", email = ""))
    override val teacher: StateFlow<Teacher> = _teacher

    // MutableStateFlow for holding student information with initial values
    private val _student = MutableStateFlow(Student(name = "", email = ""))
    override val student: StateFlow<Student> = _student

    // Function to handle the login operation with provided email and password
    override fun login(email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Use the errorHandler extension function to handle errors and return result
            val result = errorHandler {
                authService.login(email, pass)
            }

            // Check if the login was successful and emit a success message
            if(result != null){
                _success.emit("Login successfully")
            }
        }
    }

    // Function to fetch and update teacher information
    override fun getTeacher() {
        viewModelScope.launch(Dispatchers.IO) {
            // Use the errorHandler extension function to handle errors and update teacher information
            errorHandler { teacherRepo.getTeacher() }?.let {
                _teacher.value = it
            }
        }
    }

    // Function to fetch and update student information
    override fun getStudent() {
        viewModelScope.launch(Dispatchers.IO) {
            // Use the errorHandler extension function to handle errors and update student information
            errorHandler { studentRepo.getStudent() }?.let {
                _student.value = it
            }
        }
    }
}