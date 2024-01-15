package com.renbin.student_attendance_app.ui.screens.splash.viewModel

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
// HiltViewModel annotation for dependency injection
@HiltViewModel
class SplashViewModelImpl @Inject constructor(
    private val teacherRepo: TeacherRepo,
    private val studentRepo: StudentRepo,
    authService: AuthService
): BaseViewModel(), SplashViewModel {

    // MutableStateFlow to hold Teacher data
    private val _teacher = MutableStateFlow(Teacher(name = "", email = ""))
    override val teacher: StateFlow<Teacher> = _teacher

    // MutableStateFlow to hold Student data
    private val _student = MutableStateFlow(Student(name = "", email = ""))
    override val student: StateFlow<Student> = _student

    // Retrieve the authenticated user
    val auth = authService.getCurrentUser()

    // Function to fetch and provide Teacher data
    override fun getTeacher() {
        auth?.let {
            viewModelScope.launch(Dispatchers.IO) {
                // Use the errorHandler to handle potential errors during data retrieval
                errorHandler { teacherRepo.getTeacher() }?.let {
                    // Update the _teacher MutableStateFlow with the fetched Teacher data
                    _teacher.value = it
                }
            }
        }
    }

    // Function to fetch and provide Student data
    override fun getStudent() {
        auth?.let {
            viewModelScope.launch(Dispatchers.IO) {
                // Use the errorHandler to handle potential errors during data retrieval
                errorHandler { studentRepo.getStudent() }?.let {
                    // Update the _student MutableStateFlow with the fetched Student data
                    _student.value = it
                }
            }
        }
    }
}