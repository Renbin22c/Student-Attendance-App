package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.core.util.Utility.formatDatestamp
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

// Implementation of ViewModel for managing teacher-specific lesson data and actions
@HiltViewModel
class TeacherLessonViewModelImpl @Inject constructor(
    private val lessonRepo: LessonRepo,
    private val studentRepo: StudentRepo,
    private val teacherRepo: TeacherRepo,
    authService: AuthService
): BaseViewModel(), TeacherLessonViewModel, LessonViewModel {

    // StateFlow to hold the list of lessons
    private val _lessons: MutableStateFlow<List<Lesson>> = MutableStateFlow(emptyList())
    override val lessons: StateFlow<List<Lesson>> = _lessons

    // StateFlow to hold the list of students
    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    override val students: StateFlow<List<Student>> = _students

    // StateFlow to hold the list of teachers
    private val _teachers: MutableStateFlow<List<Teacher>> = MutableStateFlow(emptyList())
    override val teachers: StateFlow<List<Teacher>> = _teachers

    // StateFlow to hold the list of available classes
    private val _classes: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    override val classes: StateFlow<List<String>> = _classes

    // StateFlow to hold the filtered list of lessons based on selected class
    private val _filterLessons: MutableStateFlow<List<Lesson>> = MutableStateFlow(emptyList())
    override val filterLessons: StateFlow<List<Lesson>> = _filterLessons

    // StateFlow to hold the list of time
    private val _time: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    override val time: StateFlow<List<String>> = _time

    // Variable to store the selected time for filtering lessons
    private var _timeSelect: MutableStateFlow<String?> = MutableStateFlow(null)
    override val timeSelect: StateFlow<String?> = _timeSelect

    // Variable to store the selected class for filtering lessons
    private var _classSelect: MutableStateFlow<String?> = MutableStateFlow(null)
    override val classSelect: StateFlow<String?> = _classSelect

    // Current user retrieved from authentication service
    val user = authService.getCurrentUser()

    // Perform necessary setup actions when the ViewModel is created
    override fun onCreate() {
        super.onCreate()
        // Fetch lessons, students, and teachers data
        getAllLesson()
        getAllStudents()
        getAllTeachers()
        getTime()
    }

    // Fetch all lessons, filter by class if selected, and update related data
    override fun getAllLesson() {
        viewModelScope.launch {
            // Show loading indicator
            _loading.emit(true)
            // Fetch all lessons from the repository and collect the result
            errorHandler { lessonRepo.getAllLessons() }?.collect {
                // Sort lessons by date in descending order
                var filteredLessons = it.sortedByDescending { lesson -> lesson.date }
                // Update the list of all lessons
                _lessons.value = filteredLessons
                // If a specific class is selected, filter lessons accordingly
                if (classSelect.value != null) {
                    filteredLessons = filteredLessons.filter { lesson ->
                        lesson.classes == classSelect.value
                    }
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

                // Update the list of filtered lessons
                _filterLessons.value = filteredLessons
                // Fetch and update the list of distinct classes
                getClasses()
                // Hide loading indicator
                _loading.emit(false)
            }
        }
    }

    // Fetch and update the list of distinct classes
    override fun getClasses() {
        viewModelScope.launch(Dispatchers.IO) {
            // Extract distinct class names from the list of lessons
            val distinctClasses = _lessons.value.map { it.classes }.distinct()
            // Update the list of distinct classes
            _classes.value = distinctClasses
        }
    }

    // Fetch all student data and update the list of students
    override fun getAllStudents() {
        viewModelScope.launch {
            errorHandler { studentRepo.getAllStudents() }?.collect {
                // Update the list of students
                _students.value = it
            }
        }
    }

    // Fetch all teacher data and update the list of teachers
    override fun getAllTeachers() {
        viewModelScope.launch {
            errorHandler { teacherRepo.getAllTeachers() }?.collect {
                // Update the list of teachers
                _teachers.value = it
            }
        }
    }

    // Delete a lesson with the specified ID
    override fun deleteLesson(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Attempt to delete the lesson with the given ID from the repository
            errorHandler { lessonRepo.deleteLesson(id) }
            // Notify success upon successful deletion
            _success.emit("Delete Lesson Successfully")
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

    // Update the selected class and refresh lesson data accordingly
    override fun updateClassSelect(newClass: String?) {
        _classSelect.value = newClass
        getAllLesson()
    }


}