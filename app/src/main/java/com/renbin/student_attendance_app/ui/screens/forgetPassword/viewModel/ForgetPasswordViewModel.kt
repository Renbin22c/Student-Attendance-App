package com.renbin.student_attendance_app.ui.screens.forgetPassword.viewModel

import com.google.android.gms.tasks.Task

interface ForgetPasswordViewModel {
    fun sendPasswordResetEmail(email: String): Task<Void>
}