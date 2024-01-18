package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.repo.classes.ClassesRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

// Dagger Hilt ViewModel annotation for dependency injection
@HiltViewModel
class AddClassesViewModelImpl @Inject constructor(
    private val classesRepo: ClassesRepo,
    private val authService: AuthService
): BaseViewModel(), AddClassesViewModel {
    // Function to add a new class with the specified name
    override fun addClasses(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Perform validation on the class name
            val error = validation(name)
            if (error != null){
                // Emit an error message if validation fails
                _error.emit(error)
            } else{
                // Retrieve the current user and add the class with the specified name
                val user = authService.getCurrentUser()
                user?.let{
                    // Use the errorHandler function to handle potential errors during class addition
                    errorHandler { classesRepo.addClasses(Classes(name = name, teacher = it.uid)) }

                    // Emit a success message if class addition is successful
                    _success.emit("Add Class Successfully")
                }
            }
        }
    }

    // Function to perform validation on the class name
    override fun validation(name: String): String? {
        // Check if the class name is empty and return an error message if so
        if (name.isEmpty())(
            return  "Class name cannot be empty"
        )
        return null
    }
}