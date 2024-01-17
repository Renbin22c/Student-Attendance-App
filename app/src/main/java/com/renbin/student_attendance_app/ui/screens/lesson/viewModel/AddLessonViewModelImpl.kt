package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.core.util.NotificationUtil.createNotificationBuilder
import com.renbin.student_attendance_app.core.util.NotificationUtil.notify
import com.renbin.student_attendance_app.core.util.Utility.formatDatestamp
import com.renbin.student_attendance_app.core.util.Utility.formatTimestamp
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.repo.classes.ClassesRepo
import com.renbin.student_attendance_app.data.repo.lesson.LessonRepo
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

// HiltViewModel annotation for dependency injection
@HiltViewModel
class AddLessonViewModelImpl @Inject constructor(
    private val classesRepo: ClassesRepo,
    private val studentRepo: StudentRepo,
    private val lessonRepo: LessonRepo,
    authService: AuthService
): BaseViewModel(), AddLessonViewModel {
    // MutableStateFlow to hold the list of available classes' names
    private val _classesName = MutableStateFlow<List<String>>(emptyList())
    override val classesName: StateFlow<List<String>> = _classesName

    // Get the current user from the authentication service
    private val user = authService.getCurrentUser()

    // Function called when the ViewModel is created
    override fun onCreate() {
        super.onCreate()
        // Fetch and set the list of available classes' names
        getAllClassesName()
    }

    // Fetch all available classes' names and update the StateFlow
    override fun getAllClassesName() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                classesRepo.getAllClassesName()
            }?.collect {
                _classesName.value = it
            }
        }
    }

    // Add a new lesson with the provided information
    override fun addLesson(
        name: String, details: String, classes: String,
        date: String, time: String, context: Context
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            // Validate lesson information
            val error = lessonValidation(name, details, classes, date, time)

            // Fetch all students in the selected class
            val students = studentRepo.getAllStudentByClass(classes)
                .mapNotNull { student -> student.id }

            // Handle validation error
            if (error != null) {
                _error.emit(error)
            } else {
                // Check if there are students in the class
                if (students.isNotEmpty()) {
                    // Add the lesson to the repository
                    errorHandler {
                        lessonRepo.addLesson(
                            Lesson(
                                name = name, details = details, time = time,
                                date = date, classes = classes, student = students,
                                attendanceTime = List(students.size) { "---" },
                                attendance = List(students.size) { false },
                                createdBy = user?.uid.toString()
                            )
                        )
                    }
                    // Emit success message
                    _success.emit("Add Lesson Successfully")
                } else {
                    // Emit error if the class has no students
                    _error.emit("This class doesn't have any student")
                }
            }
        }
    }

    // Perform validation on lesson information and return an error message if validation fails
    override fun lessonValidation(
        name: String, details: String, classes: String, date: String, time: String
    ): String? {
        // Get the current date and time in the required format
        val currentDate = formatDatestamp(System.currentTimeMillis())
        val currentTime = formatTimestamp(System.currentTimeMillis())

        // Basic validations
        if (name.isEmpty()) return "Invalid Name"
        if (details.isEmpty()) return "Invalid Details"
        if (classes.isEmpty()) return "Please choose class"
        if (date.isEmpty()) return "Invalid Date"
        if (time.isEmpty()) return "Invalid Time"

        // Validate date and time
        val errorDate = isValidDate(date, currentDate)
        if (errorDate != null) return errorDate

        val errorTime = isValidTime(time, currentTime)
        if (errorTime != null) return errorTime

        // No validation errors
        return null
    }

    // Validate if the input date is after the current date
    private fun isValidDate(inputDate: String, currentDate: String): String? {
        // Define date format for parsing date strings
        val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)

        // Parse the current date and input date
        val currentDateFormatted = inputDateFormat.parse(currentDate)
        val inputDateFormatted = inputDateFormat.parse(inputDate)

        // Check if the input date is before or equal to the current date
        if (inputDateFormatted != null) {
            if (inputDateFormatted.before(currentDateFormatted) || inputDateFormatted == currentDateFormatted) {
                // Return an error message if the date is invalid
                return "Invalid Date, please choose after $currentDate"
            }
        }

        // No validation errors
        return null
    }

    // Validate if the input time is between 8:00 am and 8:00 pm
    private fun isValidTime(inputTime: String, currentTime: String): String? {
        // Define date format for parsing time strings
        val inputTimeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

        // Parse the current time, input time, start time, and end time
        val currentTimeFormat = inputTimeFormat.parse(currentTime)
        val inputTimeFormatted = inputTimeFormat.parse(inputTime)
        val startTime = inputTimeFormat.parse("8:00 am")
        val endTime = inputTimeFormat.parse("8:00 pm")

        // Check if all parsed values are non-null
        if (inputTimeFormatted != null && currentTimeFormat != null && startTime != null && endTime != null) {
            // Check if the input time is before the start time or after the end time
            if (inputTimeFormatted.before(startTime) || inputTimeFormatted.after(endTime)) {
                // Return an error message if the time is invalid
                return "Invalid Time, please choose a time between 8:00 am and 8:00 pm"
            }
        }

        // No validation errors
        return null
    }


}