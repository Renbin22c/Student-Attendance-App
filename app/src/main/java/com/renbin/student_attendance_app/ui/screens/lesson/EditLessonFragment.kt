package com.renbin.student_attendance_app.ui.screens.lesson

import androidx.fragment.app.viewModels
import com.renbin.student_attendance_app.ui.screens.lesson.viewModel.EditLessonViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditLessonFragment: BaseAddEditLessonFragment() {
    override val viewModel: EditLessonViewModelImpl by viewModels()
}