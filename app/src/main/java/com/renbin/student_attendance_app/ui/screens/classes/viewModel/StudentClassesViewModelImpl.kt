package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
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
class StudentClassesViewModelImpl @Inject constructor(
    private val studentRepo: StudentRepo,
): BaseViewModel(), StudentClassesViewModel {
    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    override val students: StateFlow<List<Student>> = _students

    private val _student = MutableStateFlow(Student(name = "", email = ""))
    override val student: StateFlow<Student> = _student

    override fun onCreate() {
        super.onCreate()

        getCurrentUser()
    }

    override fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.emit(true)
            errorHandler { studentRepo.getStudent() }?.let {
                _student.value = it
                _loading.emit(false)
            }
        }
    }

    override fun getAllStudentsByClass(classes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.emit(true)
            errorHandler { studentRepo.getAllStudentByClassUseFlow(classes) }?.collect{
                _students.value = it
                _loading.emit(false)
            }
        }
    }
}