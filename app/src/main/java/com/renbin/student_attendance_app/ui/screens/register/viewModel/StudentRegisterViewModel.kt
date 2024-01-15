package com.renbin.student_attendance_app.ui.screens.register.viewModel

import kotlinx.coroutines.flow.StateFlow

// Interface defining the contract for StudentRegisterViewModel
interface StudentRegisterViewModel {
    // StateFlow representing the list of available classes' names
    val classesName: StateFlow<List<String>>

    // Function to fetch and provide a list of all available classes' names
    fun getAllClassesName()
    // Function to perform student registration with provided information
    fun studentRegister(name: String, email: String, pass: String, confirmPass: String, classes: String)
}