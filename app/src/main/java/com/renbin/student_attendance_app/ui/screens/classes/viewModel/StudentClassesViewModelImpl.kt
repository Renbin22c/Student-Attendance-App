package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Dagger Hilt ViewModel annotation for dependency injection
@HiltViewModel
class StudentClassesViewModelImpl @Inject constructor(
    private val studentRepo: StudentRepo,
): BaseViewModel(), StudentClassesViewModel {

    // MutableStateFlow to hold the list of all students, starts with an empty list
    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    // Expose an immutable StateFlow representing the list of all students
    override val students: StateFlow<List<Student>> = _students

    // MutableStateFlow to hold details of a specific student, initialized with default values
    private val _student = MutableStateFlow(Student(name = "", email = ""))
    // Expose an immutable StateFlow representing details of a specific student
    override val student: StateFlow<Student> = _student

    // Initialization logic to be executed when the ViewModel is created
    override fun onCreate() {
        super.onCreate()
        // Call the function to retrieve details of the current user when the ViewModel is created
        getCurrentUser()
    }

    // Function to retrieve details of the current user
    override fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            // Emit a loading state to notify the UI
            _loading.emit(true)

            // Use the errorHandler function to handle potential errors during data retrieval
            errorHandler { studentRepo.getStudent() }?.let {
                // Update the StateFlow with the details of the current user
                _student.value = it

                // Emit a loading state to indicate that data retrieval is complete
                _loading.emit(false)
            }
        }
    }

    // Function to retrieve all students for a given class
    override fun getAllStudentsByClass(classes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Emit a loading state to notify the UI
            _loading.emit(true)

            // Use the errorHandler function to handle potential errors during data retrieval
            errorHandler { studentRepo.getAllStudentByClassUseFlow(classes) }?.collect {
                // Update the StateFlow with the list of all students
                _students.value = it

                // Emit a loading state to indicate that data retrieval is complete
                _loading.emit(false)
            }
        }
    }
}