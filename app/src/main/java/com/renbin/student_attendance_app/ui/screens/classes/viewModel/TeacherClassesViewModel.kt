package com.renbin.student_attendance_app.ui.screens.classes.viewModel

import com.renbin.student_attendance_app.data.model.Classes
import kotlinx.coroutines.flow.StateFlow

interface TeacherClassesViewModel {
    val classes: StateFlow<List<Classes>>

    fun getAllClasses()
}