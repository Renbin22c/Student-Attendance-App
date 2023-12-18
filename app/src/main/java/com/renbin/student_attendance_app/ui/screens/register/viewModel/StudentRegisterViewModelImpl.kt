package com.renbin.student_attendance_app.ui.screens.register.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.repo.classes.ClassesRepo
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentRegisterViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val classesRepo: ClassesRepo,
    private val studentRepo: StudentRepo
): BaseViewModel(), StudentRegisterViewModel {
    private val _classesName = MutableStateFlow<List<String>>(emptyList())
    override val classesName: StateFlow<List<String>> =_classesName

    override fun onCreate() {
        super.onCreate()
        getAllClassesName()
    }
    override fun getAllClassesName() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                classesRepo.getAllClassesName()
            }?.collect{
                _classesName.value = it
            }
        }
    }

    override fun studentRegister(name: String, email: String, pass: String, confirmPass: String, classes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = errorHandler { authService.register(email, pass) }
            if(user != null){
                errorHandler {
                    studentRepo.addStudent(
                        Student(name = name, email = email, classes = classes)
                    )
                }
                _success.emit("Register successfully")
            }
        }
    }
}