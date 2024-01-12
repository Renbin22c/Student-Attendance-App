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

@HiltViewModel
class StudentDetailsEditViewModelImpl @Inject constructor(
    private val studentRepo: StudentRepo,
    private val classesRepo: ClassesRepo
): BaseViewModel(), StudentDetailsEditViewModel {
    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    override val students: StateFlow<List<Student>> = _students

    private val _classesName = MutableStateFlow<List<String>>(emptyList())
    override val classesName: StateFlow<List<String>> =_classesName

    override fun onCreate() {
        super.onCreate()

        getAllStudent()
        getAllClassesName()
    }

    override fun getAllStudent() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.emit(true)
            errorHandler { studentRepo.getAllStudents() }?.collect{
                _students.value = it
                _loading.emit(false)
            }
        }
    }

    override fun updateStudent(student: Student, classes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { studentRepo.updateStudent(
                Student(student.id ,name = student.name, email = student.email, classes = classes, createdAt = student.createdAt ))
            }
            _success.emit("Update successfully")
        }
    }

    override fun filterEmailByQuery(query: String): List<Student> {
        return _students.value.filter { student ->
            student.email.contains(query, ignoreCase = true)
        }
    }

    override fun deleteStudent(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { studentRepo.deleteStudent(id) }
            _success.emit("Delete successfully")
        }
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


}