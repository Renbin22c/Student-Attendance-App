package com.renbin.student_attendance_app.ui.screens.profile.viewModel

import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeacherProfileViewModelImpl @Inject constructor(

): BaseViewModel(), TeacherProfileViewModel {
}