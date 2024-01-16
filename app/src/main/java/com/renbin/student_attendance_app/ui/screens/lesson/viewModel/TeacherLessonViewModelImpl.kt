package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.data.repo.lesson.LessonRepo
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.data.repo.teacher.TeacherRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    // Variable to store the selected class for filtering lessons
    private var _classSelect: String? = null
    val classSelect: String? get() = _classSelect

    // Current user retrieved from authentication service
    val user = authService.getCurrentUser()

    // Perform necessary setup actions when the ViewModel is created
    override fun onCreate() {
        super.onCreate()
        // Fetch lessons, students, and teachers data
        getAllLesson()
        getAllStudents()
        getAllTeachers()
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
                // If a specific class is selected, filter lessons accordingly
                if (classSelect != null) {
                    filteredLessons = filteredLessons.filter { lesson ->
                        lesson.classes == classSelect
                    }
                }
                // Update the list of all lessons and filtered lessons
                _lessons.value = filteredLessons
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

    // Update the selected class and refresh lesson data accordingly
    fun updateClassSelect(newClassSelect: String?) {
        _classSelect = newClassSelect
        getAllLesson()
    }


}