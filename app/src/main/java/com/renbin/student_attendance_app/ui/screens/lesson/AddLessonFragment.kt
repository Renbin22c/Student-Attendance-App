package com.renbin.student_attendance_app.ui.screens.lesson

import androidx.fragment.app.viewModels
import com.renbin.student_attendance_app.ui.screens.lesson.viewModel.AddLessonViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddLessonFragment: BaseAddEditLessonFragment() {
    override val viewModel: AddLessonViewModelImpl by viewModels()
}