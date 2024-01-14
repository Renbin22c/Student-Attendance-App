package com.renbin.student_attendance_app.ui.screens.forgetPassword.viewModel

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModelImpl @Inject constructor(
) : BaseViewModel(), ForgetPasswordViewModel {
    private val auth: FirebaseAuth = Firebase.auth

    override fun sendPasswordResetEmail(email: String): Task<Void> =
        auth.sendPasswordResetEmail(email)
}