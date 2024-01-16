package com.renbin.student_attendance_app.ui.screens.student.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

// Dagger Hilt ViewModel annotation
@HiltViewModel
class StudentDetailsViewModelImpl @Inject constructor(
    private val studentRepo: StudentRepo
): BaseViewModel(), StudentDetailsViewModel {
    // MutableStateFlow to hold the list of students
    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    // Exposing the StateFlow to observe the list of students
    override val students: StateFlow<List<Student>> = _students

    // Function to be executed during ViewModel creation
    override fun onCreate() {
        super.onCreate()

        // Fetch the list of students
        getAllStudents()
    }

    // Function to retrieve all students from the repository
    override fun getAllStudents() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.emit(true)

            // Collect the result from the repository using the errorHandler extension function
            errorHandler { studentRepo.getAllStudents() }?.collect{
                // Update the StateFlow with the sorted list of students by name
                _students.value = it.sortedBy { student -> student.name }
                _loading.emit(false)
            }
        }
    }
}