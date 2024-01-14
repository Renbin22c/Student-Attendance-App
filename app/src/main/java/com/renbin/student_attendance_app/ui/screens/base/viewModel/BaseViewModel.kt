package com.renbin.student_attendance_app.ui.screens.base.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel : ViewModel() {
    // MutableSharedFlow for emitting error messages
    protected val _error: MutableSharedFlow<String> = MutableSharedFlow()
    val error: SharedFlow<String> = _error

    // MutableSharedFlow for emitting success messages
    protected val _success: MutableSharedFlow<String> = MutableSharedFlow()
    val success: SharedFlow<String> = _success

    // MutableSharedFlow for emitting loading state
    protected val _loading: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val loading: SharedFlow<Boolean> = _loading

    // Function to be called when the ViewModel is created
    open fun onCreate(){}

    // Function to handle exceptions and emit error messages
    suspend fun <T> errorHandler(callback: suspend () -> T?): T? {
        return try {
            callback()
        } catch (e: Exception) {
            // Emit an error message to the _error shared flow
            _error.emit(e.message ?: "Something went wrong")
            e.printStackTrace()
            null
        }
    }
}