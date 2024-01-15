package com.renbin.student_attendance_app.ui.screens.home.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.core.util.Utility
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.data.repo.lesson.LessonRepo
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.data.repo.teacher.TeacherRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Dagger Hilt ViewModel annotation
@HiltViewModel
class StudentHomeViewModelImpl @Inject constructor(
    private val authService: AuthService,
    private val lessonRepo: LessonRepo,
    private val studentRepo: StudentRepo,
    private val teacherRepo: TeacherRepo,
): BaseViewModel(), HomeViewModel, StudentHomeViewModel {

    // MutableStateFlow to hold the list of lessons
    private val _lessons: MutableStateFlow<List<Lesson>> = MutableStateFlow(emptyList())
    override val lessons: StateFlow<List<Lesson>> = _lessons

    // MutableStateFlow to hold the list of students
    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    override val students: StateFlow<List<Student>> = _students

    // MutableStateFlow to hold the list of teachers
    private val _teachers: MutableStateFlow<List<Teacher>> = MutableStateFlow(emptyList())
    override val teachers: StateFlow<List<Teacher>> = _teachers

    // MutableSharedFlow to emit logout success events
    private val _logoutSuccess: MutableSharedFlow<String> = MutableSharedFlow()
    override val logoutSuccess: SharedFlow<String> = _logoutSuccess

    // Get the current user from the AuthService
    val user = authService.getCurrentUser()

    // Function called when the ViewModel is created
    override fun onCreate() {
        super.onCreate()
        // Fetch and update the list of lessons, students, and teachers
        getAllLesson()
        getAllStudents()
        getAllTeachers()
    }

    // Function to fetch and update the list of lessons
    override fun getAllLesson() {
        viewModelScope.launch(Dispatchers.IO) {
            // Show loading indicator
            _loading.emit(true)
            // Fetch all lessons and filter by the current student
            errorHandler { lessonRepo.getAllLessons() }?.collect{
                val filterLesson = it.filter {lesson ->
                    lesson.student.contains(user?.uid)
                }

                // Filter lessons for today
                val lessonsToday = filterLesson.filter { lesson ->
                    lesson.date == Utility.formatDatestamp(System.currentTimeMillis())
                }
                // Update the list of lessons
                _lessons.value = lessonsToday
                // Hide loading indicator
                _loading.emit(false)
            }
        }
    }

    // Function to fetch and update the list of students
    override fun getAllStudents() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { studentRepo.getAllStudents() }?.collect{
                // Update the list of students
                _students.value = it
            }
        }
    }

    // Function to fetch and update the list of teachers
    override fun getAllTeachers() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { teacherRepo.getAllTeachers() }?.collect {
                // Update the list of teachers
                _teachers.value = it
            }
        }
    }

    // Function to handle logout
    override fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            // Perform logout using AuthService
            errorHandler { authService.logout() }
            // Emit logout success event
            _logoutSuccess.emit("Logout Successfully")
        }
    }

    // Function to update attendance status for a lesson
    override fun updateAttendanceStatus(id: String, lesson: Lesson) {
        viewModelScope.launch(Dispatchers.IO) {
            if(user != null){
                // Find the index of the current user in the lesson's student list
                val index = lesson.student.indexOf(user.uid)
                if(index != -1) {
                    // Modify attendance status and time for the current user
                    val attend = lesson.attendance.toMutableList()
                    val attendTime = lesson.attendanceTime.toMutableList()

                    attend[index] = true
                    attendTime[index] = Utility.formatTimestamp(System.currentTimeMillis())

                    val newLesson = lesson.copy(attendance = attend, attendanceTime = attendTime)

                    // Update the lesson with modified attendance information
                    errorHandler { lessonRepo.updateLesson(id,newLesson) }
                    // Emit success message
                    _success.emit("Check In Successfully")
                }
            }
        }
    }
}