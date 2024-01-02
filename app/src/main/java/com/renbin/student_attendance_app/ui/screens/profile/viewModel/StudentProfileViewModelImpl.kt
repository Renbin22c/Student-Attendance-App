package com.renbin.student_attendance_app.ui.screens.profile.viewModel

import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.auth.User
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentProfileViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val studentRepo: StudentRepo
): BaseViewModel(), StudentProfileViewModel {
    private val _user = MutableStateFlow(Student(name = "Unknown", email = "Unknown"))
    override val user: StateFlow<Student>  = _user

    init {
        getCurrentUser()
    }

    override fun getCurrentUser() {
        val firebaseUser = authService.getCurrentUser()
        firebaseUser?.let{
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler { studentRepo.getStudent() }?.let{user ->
                    _user.value = user
                }
            }
        }
    }

}