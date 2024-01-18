package com.renbin.student_attendance_app.ui.screens.profile.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.core.service.StorageService
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
    private val teacherRepo: TeacherRepo,
    private val storageService: StorageService

): BaseViewModel(), TeacherProfileViewModel {

    private val _user = MutableStateFlow(Teacher(name = "Unknown", email = "Unknown"))
    override val user: MutableStateFlow<Teacher> = _user
    private val _profileUri = MutableStateFlow<Uri?>(null)
    val profileUri: StateFlow<Uri?> = _profileUri

    init {
        getProfilePicUri()
        getCurrentUser()
    }

    fun getUserName(): String {
        return _user.value.name
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

    fun updateProfile(name: String?, imageUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = teacherRepo.getTeacher()

            val updatedUser = currentUser?.copy()

            name?.let { updatedUser?.name = it }

            imageUri?.let {
                val userId = authService.getCurrentUser()?.uid
                val imageName = "$userId.jpg"
                updatedUser?.profilePicUrl = imageName
                storageService.saveSelectedImageUri(imageName, it)
                getProfilePicUri()
            }

            errorHandler {
                if (updatedUser != null) {
                    teacherRepo.updateTeacher(updatedUser)
                }
            }

            getCurrentUser()
        }
    }

    fun getProfilePicUri() {
        viewModelScope.launch(Dispatchers.IO) {
            authService.getCurrentUser()?.uid?.let {
                val imageName = "$it.jpg"
                _profileUri.value = storageService.loadSelectedImageUri(imageName)

                Log.d("image", imageName)
                Log.d("profilepic", profileUri.value.toString())
            }
        }
    }
}