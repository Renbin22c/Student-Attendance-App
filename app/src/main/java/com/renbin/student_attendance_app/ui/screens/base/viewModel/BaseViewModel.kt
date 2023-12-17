package com.renbin.student_attendance_app.ui.screens.base.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel : ViewModel() {
    protected val _error: MutableSharedFlow<String> = MutableSharedFlow()
    val error: SharedFlow<String> = _error

    protected val _success: MutableSharedFlow<String> = MutableSharedFlow()
    val success: SharedFlow<String> = _success

    protected val _loading: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val loading: SharedFlow<Boolean> = _loading

    open fun onCreate(){}

    suspend fun <T> errorHandler(callback: suspend () -> T?): T? {
        return try {
            callback()
        } catch (e: Exception) {
            _error.emit(e.message ?: "Something went wrong")
            e.printStackTrace()
            null
        }
    }
}