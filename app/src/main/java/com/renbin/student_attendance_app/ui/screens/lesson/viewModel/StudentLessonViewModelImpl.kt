package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.core.util.Utility.formatDatestamp
import com.renbin.student_attendance_app.core.util.Utility.formatTimestamp
import com.renbin.student_attendance_app.core.util.Utility.parseDatestampFromString
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.data.repo.lesson.LessonRepo
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.data.repo.teacher.TeacherRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel implementation for managing student lessons, attendance, and related data
@HiltViewModel
class StudentLessonViewModelImpl @Inject constructor(
    private val lessonRepo: LessonRepo,
    private val studentRepo: StudentRepo,
    private val teacherRepo: TeacherRepo,
    authService: AuthService
) : BaseViewModel(), StudentLessonViewModel, LessonViewModel {
    // MutableStateFlow to hold the list of lessons
    private val _lessons: MutableStateFlow<List<Lesson>> = MutableStateFlow(emptyList())
    override val lessons: StateFlow<List<Lesson>> = _lessons

    // MutableStateFlow to hold the list of students
    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    override val students: StateFlow<List<Student>> = _students

    // MutableStateFlow to hold the list of teachers
    private val _teachers: MutableStateFlow<List<Teacher>> = MutableStateFlow(emptyList())
    override val teachers: StateFlow<List<Teacher>> = _teachers

    // MutableStateFlow to hold the list of time
    private val _time: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    override val time: StateFlow<List<String>> = _time

    // Variable to store the selected time for filtering lessons
    private var _timeSelect: MutableStateFlow<String?> = MutableStateFlow(null)
    override val timeSelect: StateFlow<String?> = _timeSelect

    // Get the current user from the authentication service
    val user = authService.getCurrentUser()

    // Function called on ViewModel creation to fetch initial data
    override fun onCreate() {
        super.onCreate()
        getAllLesson()
        getAllStudents()
        getAllTeachers()
        getTime()
    }

    // Function to fetch all lessons associated with the current student
    override fun getAllLesson() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.emit(true)
            errorHandler { lessonRepo.getAllLessons() }?.collect {
                // Filter lessons to only include those associated with the current student
                var filteredLessons = it.filter { lesson ->
                    lesson.student.contains(user?.uid)
                }

                if (timeSelect.value != null) {
                    val currentTimeMillis = System.currentTimeMillis()
                    when (timeSelect.value) {
                        "~${formatDatestamp(currentTimeMillis)} (old)" -> {
                            // Filter lessons before today
                            filteredLessons = filteredLessons.filter { lesson ->
                                parseDatestampFromString(lesson.date) < currentTimeMillis
                            }
                        }

                        "${formatDatestamp(currentTimeMillis)}~ (new)" -> {
                            // Filter lessons after today
                            filteredLessons = filteredLessons.filter { lesson ->
                                parseDatestampFromString(lesson.date) > currentTimeMillis
                            }
                        }

                        else -> {
                            // Handle other cases or no time selection
                            filteredLessons = filteredLessons.filter { lesson ->
                                lesson.date == formatDatestamp(currentTimeMillis)
                            }

                        }
                    }
                }
                _lessons.value = filteredLessons
                _loading.emit(false)
            }
        }
    }

    // Function to fetch all students
    override fun getAllStudents() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { studentRepo.getAllStudents() }?.collect {
                _students.value = it
            }
        }
    }

    // Function to fetch all teachers
    override fun getAllTeachers() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { teacherRepo.getAllTeachers() }?.collect {
                _teachers.value = it
            }
        }
    }

    // Function to update the attendance status of the current student in a lesson
    override fun updateAttendanceStatus(id: String, lesson: Lesson) {
        viewModelScope.launch(Dispatchers.IO) {
            if (user != null) {
                val index = lesson.student.indexOf(user.uid)
                if (index != -1) {
                    val attend = lesson.attendance.toMutableList()
                    val attendTime = lesson.attendanceTime.toMutableList()

                    // Mark the student as attended and record the attendance time
                    attend[index] = true
                    attendTime[index] = formatTimestamp(System.currentTimeMillis())

                    val newLesson = lesson.copy(attendance = attend, attendanceTime = attendTime)

                    // Update the lesson with the new attendance information
                    errorHandler { lessonRepo.updateLesson(id, newLesson) }
                    _success.emit("Check In Successfully")
                }
            }
        }
    }

    // Get today date and specific to old, current and new
    override fun getTime() {
        viewModelScope.launch(Dispatchers.IO) {
            val listTime = listOf(
                "~${formatDatestamp(System.currentTimeMillis())} (old)",
                "${formatDatestamp(System.currentTimeMillis())} (current)",
                "${formatDatestamp(System.currentTimeMillis())}~ (new)")

            _time.value = listTime
        }
    }

    // Update the selected time and refresh lesson data accordingly
    override fun updateTimeSelect(newTime: String?){
        _timeSelect.value = newTime
        getAllLesson()

    }
}