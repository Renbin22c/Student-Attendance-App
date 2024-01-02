package com.renbin.student_attendance_app.ui.screens.profile.viewModel

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
class TeacherProfileViewModelImpl @Inject constructor(

    private val authService: AuthService,
    private val teacherRepo: TeacherRepo

): BaseViewModel(), TeacherProfileViewModel {

    private val _user = MutableStateFlow(Teacher(name = "Unknown", email = "Unknown"))
    override val user: MutableStateFlow<Teacher> = _user

    init {
        getCurrentUser()
    }

    override fun getCurrentUser() {
        val firebaseUser = authService.getCurrentUser()
        firebaseUser?.let{
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler { teacherRepo.getTeacher() }?.let{user ->
                    _user.value = user
                }
            }
        }
    }
}