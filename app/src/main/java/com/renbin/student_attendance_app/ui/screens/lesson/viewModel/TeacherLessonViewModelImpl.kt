package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeacherLessonViewModelImpl @Inject constructor(

): BaseViewModel(), TeacherLessonViewModel {
}