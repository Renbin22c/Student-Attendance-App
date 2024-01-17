package com.renbin.student_attendance_app.ui.screens.profile.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.core.service.StorageService
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
    private val studentRepo: StudentRepo,
    private val storageService: StorageService
) : BaseViewModel(), StudentProfileViewModel {
    private val _user = MutableStateFlow(Student(name = "Unknown", email = "Unknown"))
    override val user: StateFlow<Student> = _user
    private val _profileUri = MutableStateFlow<Uri?>(null)
    val profileUri: StateFlow<Uri?> = _profileUri

    init {
        getCurrentUser()
        getProfilePicUri()
    }

    override fun getCurrentUser() {
        val firebaseUser = authService.getCurrentUser()
        firebaseUser?.let {
            viewModelScope.launch(Dispatchers.IO) {
                errorHandler { studentRepo.getStudent() }?.let { user ->
                    _user.value = user
                }
            }
        }
    }

    fun getUserName(): String {
        return _user.value.name
    }


    fun updateProfile(name: String?, imageUri: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = studentRepo.getStudent()

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
                    studentRepo.updateStudent(updatedUser)
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
