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

@HiltViewModel
class ClassesDetailsViewModelImpl @Inject constructor(
    private val studentRepo: StudentRepo
): BaseViewModel(), ClassesDetailsViewModel {
    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    override val students: StateFlow<List<Student>> = _students

    override fun getAllStudentsByClass(classes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.emit(true)
            errorHandler { studentRepo.getAllStudentByClassUseFlow(classes) }?.collect{
                _students.value = it.sortedBy { student -> student.name }
                _loading.emit(false)
            }
        }
    }

}