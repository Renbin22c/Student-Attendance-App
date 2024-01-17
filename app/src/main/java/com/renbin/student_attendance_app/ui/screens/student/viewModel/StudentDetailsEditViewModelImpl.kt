package com.renbin.student_attendance_app.ui.screens.student.viewModel

import androidx.lifecycle.viewModelScope
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

// Dagger Hilt ViewModel annotation
@HiltViewModel
class StudentDetailsEditViewModelImpl @Inject constructor(
    private val studentRepo: StudentRepo,
    private val classesRepo: ClassesRepo
): BaseViewModel(), StudentDetailsEditViewModel {
    // MutableStateFlow representing the list of students
    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    override val students: StateFlow<List<Student>> = _students

    // MutableStateFlow representing the list of class names
    private val _classesName = MutableStateFlow<List<String>>(emptyList())
    override val classesName: StateFlow<List<String>> = _classesName


     // Initialization function to fetch initial data when the ViewModel is created.
    override fun onCreate() {
        super.onCreate()

        // Fetch all students and class names
        getAllStudent()
        getAllClassesName()
    }


     // Function to retrieve all students from the repository.
    override fun getAllStudent() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.emit(true)
            errorHandler { studentRepo.getAllStudents() }?.collect{
                _students.value = it
                _loading.emit(false)
            }
        }
    }

    // Function to update a student's details in the repository.
    override fun updateStudent(student: Student, classes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                studentRepo.updateStudent(
                    Student(student.id, name = student.name, email = student.email, classes = classes, createdAt = student.createdAt)
                )
            }
            _success.emit("Update successfully")
        }
    }


    // Function to filter students by email based on a query.
    override fun filterEmailByQuery(query: String): List<Student> {
        return _students.value.filter { student ->
            student.email.contains(query, ignoreCase = true)
        }
    }


    // Function to delete a student by ID in the repository.
    override fun deleteStudent(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { studentRepo.deleteStudent(id) }
            _success.emit("Delete successfully")
        }
    }


    // Function to retrieve all class names from the repository.
    override fun getAllClassesName() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                classesRepo.getAllClassesName()
            }?.collect{
                _classesName.value = it
            }
        }
    }
}