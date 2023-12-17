package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.repo.classes.ClassesRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddClassesViewModelImpl @Inject constructor(
    private val classesRepo: ClassesRepo,
    private val authService: AuthService
): BaseViewModel(), AddClassesViewModel {
    override fun addClasses(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = authService.getCurrentUser()
            user?.let{
                errorHandler { classesRepo.addClasses(Classes(name= name, teacher = it.uid)) }
                _success.emit("Add Class Successfully")
            }
        }
    }
}