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

// Hilt ViewModel annotation for dependency injection
@HiltViewModel
class StudentRegisterViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val classesRepo: ClassesRepo,
    private val studentRepo: StudentRepo
): BaseViewModel(), StudentRegisterViewModel {

    // MutableStateFlow to hold the list of available classes' names
    private val _classesName = MutableStateFlow<List<String>>(emptyList())
    override val classesName: StateFlow<List<String>> =_classesName

    // Function called when the ViewModel is created
    override fun onCreate() {
        super.onCreate()
        // Fetch and initialize the list of available classes' names
        getAllClassesName()
    }

    // Function to fetch and provide a list of all available classes' names
    override fun getAllClassesName() {
        viewModelScope.launch(Dispatchers.IO) {
            // Use the errorHandler extension function to handle errors during classes' names retrieval
            errorHandler {
                classesRepo.getAllClassesName()
            }?.collect{
                // Update the _classesName StateFlow with the retrieved classes' names
                _classesName.value = it
            }
        }
    }

    // Function to perform student registration with provided information
    override fun studentRegister(name: String, email: String, pass: String, confirmPass: String, classes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val error = validation(name, email, pass, confirmPass, classes)

            if(error !=null){
                _error.emit(error)
            }else{
                // Use the errorHandler extension function to handle errors during registration
                val user = errorHandler { authService.register(email, pass) }
                if(user != null){
                    // Use the errorHandler extension function to handle errors during student registration
                    errorHandler {
                        studentRepo.addStudent(
                            Student(name = name, email = email, classes = classes)
                        )
                    }
                    // Emit a success message
                    _success.emit("Register successfully")
                }
            }
        }
    }

    private fun validation(name: String, email: String, pass: String, confirmPass: String, classes: String): String?{
        return if(name.isEmpty()){
            "Name cannot be empty"
        } else if (email.isEmpty()){
            "Email cannot be empty"
        } else if (pass.isEmpty()){
            "Password cannot be empty"
        } else if(confirmPass.isEmpty()){
            "Confirm password cannot be empty"
        } else if (pass != confirmPass){
            "Password and Confirm Password must be same"
        }else if (classes.isEmpty()){
            "Class cannot be empty"
        }else{
            null
        }
    }
}