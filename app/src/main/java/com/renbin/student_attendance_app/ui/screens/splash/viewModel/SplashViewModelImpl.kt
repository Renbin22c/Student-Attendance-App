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
@HiltViewModel
class SplashViewModelImpl @Inject constructor(
    private val teacherRepo: TeacherRepo,
    private val studentRepo: StudentRepo,
    authService: AuthService
): BaseViewModel(), SplashViewModel {
    private val _teacher = MutableStateFlow(Teacher(name = "", email = ""))
    override val teacher: StateFlow<Teacher> = _teacher

    private val _student = MutableStateFlow(Student(name = "", email = ""))
    override val student: StateFlow<Student> = _student

    val auth = authService.getCurrentUser()
    override fun getTeacher() {
        auth?.let {
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler { teacherRepo.getTeacher() }?.let {
                    _teacher.value = it
                }
            }
        }
    }

    override fun getStudent() {
        auth?.let {
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler { studentRepo.getStudent() }?.let {
                    _student.value = it
                }
            }
        }
    }
}