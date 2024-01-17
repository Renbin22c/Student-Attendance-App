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
class ClassesDetailsViewModelImpl @Inject constructor(
    private val studentRepo: StudentRepo
): BaseViewModel(), ClassesDetailsViewModel {

    // MutableStateFlow to hold the list of students, starts with an empty list
    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    // Expose an immutable StateFlow representing the list of students
    override val students: StateFlow<List<Student>> = _students

    // Function to fetch all students for a given class
    override fun getAllStudentsByClass(classes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Emit a loading state to notify the UI
            _loading.emit(true)

            // Use the errorHandler function to handle potential errors during data retrieval
            errorHandler { studentRepo.getAllStudentByClassUseFlow(classes) }?.collect {
                // Update the list of students in the StateFlow, sorted by student name
                _students.value = it.sortedBy { student -> student.name }

                // Emit a loading state to indicate that data retrieval is complete
                _loading.emit(false)
            }
        }
    }
}