package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.repo.ClassesRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassesViewModelImpl @Inject constructor(
    private val classesRepo: ClassesRepo
): BaseViewModel(), ClassesViewModel {
    private val _classes: MutableStateFlow<List<Classes>> = MutableStateFlow(emptyList())
    override val classes: StateFlow<List<Classes>> = _classes

    override fun onCreate() {
        super.onCreate()
        getAllClasses()
    }


    override fun getAllClasses() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                classesRepo.getAllClasses()
            }?.collect{
                _classes.value = it
            }
        }
    }
}