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

@HiltViewModel
class TeacherClassesViewModelImpl @Inject constructor(
    private val classesRepo: ClassesRepo,
    private val teacherRepo: TeacherRepo,
    private val studentRepo: StudentRepo
): BaseViewModel(), TeacherClassesViewModel {
    private val _classes: MutableStateFlow<List<Classes>> = MutableStateFlow(emptyList())
    override val classes: StateFlow<List<Classes>> = _classes

    private val _teachers: MutableStateFlow<List<Teacher>> = MutableStateFlow(emptyList())
    override val teachers: StateFlow<List<Teacher>> = _teachers

    override fun onCreate() {
        super.onCreate()
        getAllClasses()
        getAllTeachers()
    }


    override fun getAllClasses() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { classesRepo.getAllClasses() }?.collect{
                _classes.value = it
            }
        }
    }

    override fun deleteClasses(id: String, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val students = studentRepo.getAllStudentByClass(name)
                .mapNotNull { student -> student.id }
            if (students.isEmpty()){
                errorHandler { classesRepo.deleteClasses(id) }
                _success.emit("Delete class successfully")
            } else{
                _error.emit("Still got student in class")
            }
        }
    }

    override fun getAllTeachers() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { teacherRepo.getAllTeachers() }?.collect {
                _teachers.value = it
            }
        }
    }
}