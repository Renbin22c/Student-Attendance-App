package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.data.repo.classes.ClassesRepo
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.data.repo.teacher.TeacherRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Dagger Hilt ViewModel annotation for dependency injection
@HiltViewModel
class TeacherClassesViewModelImpl @Inject constructor(
    private val classesRepo: ClassesRepo,
    private val teacherRepo: TeacherRepo,
    private val studentRepo: StudentRepo
): BaseViewModel(), TeacherClassesViewModel {

    // MutableStateFlow to hold the list of classes, starts with an empty list
    private val _classes: MutableStateFlow<List<Classes>> = MutableStateFlow(emptyList())
    // Expose an immutable StateFlow representing the list of classes
    override val classes: StateFlow<List<Classes>> = _classes

    // MutableStateFlow to hold the list of teachers, starts with an empty list
    private val _teachers: MutableStateFlow<List<Teacher>> = MutableStateFlow(emptyList())
    // Expose an immutable StateFlow representing the list of teachers
    override val teachers: StateFlow<List<Teacher>> = _teachers

    // MutableStateFlow to indicate whether the list of students for a class is empty, starts with false
    private val _isStudentsEmpty: MutableStateFlow<Boolean> = MutableStateFlow(false)
    // Expose an immutable StateFlow representing whether the list of students for a class is empty
    override val isStudentsEmpty: StateFlow<Boolean> = _isStudentsEmpty

    // Initialization logic to be executed when the ViewModel is created
    override fun onCreate() {
        super.onCreate()
        // Call functions to retrieve all classes and all teachers when the ViewModel is created
        getAllClasses()
        getAllTeachers()
    }

    // Function to retrieve all classes
    override fun getAllClasses() {
        viewModelScope.launch(Dispatchers.IO) {
            // Emit a loading state to notify the UI
            _loading.emit(true)

            // Use the errorHandler function to handle potential errors during data retrieval
            errorHandler { classesRepo.getAllClasses() }?.collect {
                // Update the StateFlow with the list of classes
                _classes.value = it

                // Emit a loading state to indicate that data retrieval is complete
                _loading.emit(false)
            }
        }
    }

    // Function to check if the list of students for a class is empty
    override fun checkClassStudents(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Retrieve the list of student IDs for the specified class
            val students = studentRepo.getAllStudentByClass(name)
                .mapNotNull { student -> student.id }

            // Update the StateFlow to indicate whether the list of students for a class is empty
            _isStudentsEmpty.value = students.isEmpty()
        }
    }

    // Function to delete a class by its ID
    override fun deleteClasses(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Use the errorHandler function to handle potential errors during class deletion
            errorHandler { classesRepo.deleteClasses(id) }

            // Emit a success message if class deletion is successful
            _success.emit("Delete class successfully")
        }
    }

    // Function to retrieve all teachers
    override fun getAllTeachers() {
        viewModelScope.launch(Dispatchers.IO) {
            // Use the errorHandler function to handle potential errors during data retrieval
            errorHandler { teacherRepo.getAllTeachers() }?.collect {
                // Update the StateFlow with the list of teachers
                _teachers.value = it
            }
        }
    }
}