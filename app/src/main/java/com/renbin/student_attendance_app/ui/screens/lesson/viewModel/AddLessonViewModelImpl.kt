package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.core.util.Utility.formatDatestamp
import com.renbin.student_attendance_app.core.util.Utility.formatTimestamp
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
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

@HiltViewModel
class AddLessonViewModelImpl @Inject constructor(
    private val classesRepo: ClassesRepo,
    private val studentRepo: StudentRepo,
    private val lessonRepo: LessonRepo,
    private val authService: AuthService
): BaseViewModel(), AddLessonViewModel{
    private val _classesName = MutableStateFlow<List<String>>(emptyList())
    override val classesName: StateFlow<List<String>> =_classesName

    private val user = authService.getCurrentUser()

    override fun onCreate() {
        super.onCreate()
        getAllClassesName()
    }
    override fun getAllClassesName() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                classesRepo.getAllClassesName()
            }?.collect{
                _classesName.value = it
            }
        }
    }

    override fun addLesson(name: String, details: String, classes: String,
        date: String, time: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val error = lessonValidation(name, details, classes, date, time)

            val students = studentRepo.getAllStudentByClass(classes)
                .mapNotNull { student -> student.id }

            if(error != null){
                _error.emit(error)
            } else{
                if(students.isNotEmpty()) {
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
                    _success.emit("Add Lesson Successfully")
                } else{
                    _error.emit("This class don't have any student")
                }
            }
        }
    }

    override fun lessonValidation(name: String, details: String, classes: String, date: String, time: String): String? {
        val currentDate = formatDatestamp(System.currentTimeMillis())
        val currentTime = formatTimestamp(System.currentTimeMillis())

        if (name.isEmpty()) return "Invalid Name"
        if (details.isEmpty()) return "Invalid Details"
        if (classes.isEmpty()) return "Please choose class"

        val errorDate = isValidDate(date, currentDate)
        if(errorDate != null) return errorDate
        val errorTime = isValidTime(time, currentTime)

        return null
    }

    private fun isValidDate(inputDate: String, currentDate: String): String? {
        val inputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        val currentDateFormatted = inputDateFormat.parse(currentDate)
        val inputDateFormatted = inputDateFormat.parse(inputDate)

        if (inputDateFormatted != null) {
            if(inputDateFormatted.before(currentDateFormatted) || inputDateFormatted.equals(currentDateFormatted)){
                    return "Invalid Date, please choose after $currentDate"
            }
        }

        return null
    }

    private fun isValidTime(inputTime: String, currentTime: String): String? {
        val inputTimeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val currentTimeFormat = inputTimeFormat.parse(currentTime)
        val inputTimeFormatted = inputTimeFormat.parse(inputTime)

        val startTime = inputTimeFormat.parse("8:00 am")
        val endTime = inputTimeFormat.parse("8:00 pm")

        Log.d("debugging", "Input Time: $inputTimeFormatted, Current Time: $currentTimeFormat")
        Log.d("debugging", "Start Time: $startTime, End Time: $endTime")

        return null
    }


}