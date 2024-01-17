package com.renbin.student_attendance_app.ui.screens.classes.viewModel

// Define a ViewModel interface for handling class-related operations
interface AddClassesViewModel {
    // Function to add a new class with the specified name
    fun addClasses(name: String)

    // Function to perform validation on the class name
    fun validation(name: String) :String?
}