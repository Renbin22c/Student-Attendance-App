package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherLessonViewModelImpl @Inject constructor(
    private val lessonRepo: LessonRepo,
    private val studentRepo: StudentRepo,
    private val teacherRepo: TeacherRepo,
    authService: AuthService
): BaseViewModel(), TeacherLessonViewModel, LessonViewModel {
    private val _lessons: MutableStateFlow<List<Lesson>> = MutableStateFlow(emptyList())
    override val lessons: StateFlow<List<Lesson>> = _lessons

    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    override val students: StateFlow<List<Student>> = _students

    private val _teachers: MutableStateFlow<List<Teacher>> = MutableStateFlow(emptyList())
    override val teachers: StateFlow<List<Teacher>> = _teachers

    private val _classes: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    override val classes: StateFlow<List<String>> = _classes

    private val _dates: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    override val dates: StateFlow<List<String>> = _dates

    private val _filteredLessons: MutableStateFlow<List<Lesson>> = MutableStateFlow(emptyList())
    override val filteredLessons: StateFlow<List<Lesson>> = _filteredLessons

    val user = authService.getCurrentUser()

    override fun onCreate() {
        super.onCreate()
        getAllLesson()
        getAllStudents()
        getAllTeachers()
    }

    override fun getAllLesson() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { lessonRepo.getAllLessons() }?.collect{
                _lessons.value = it.sortedByDescending{ lesson -> lesson.date  }
                getClassesAndDates()
            }
        }
    }

    override fun getClassesAndDates() {
        viewModelScope.launch(Dispatchers.IO) {
            val distinctClasses = _lessons.value.map { it.classes }.distinct()
            val distinctDates = _lessons.value.map { it.date }.distinct()

            _classes.value = distinctClasses
            _dates.value = distinctDates
        }
    }

    override fun getAllStudents() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { studentRepo.getAllStudents() }?.collect{
                _students.value = it
            }
        }
    }

    override fun getAllTeachers() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { teacherRepo.getAllTeachers() }?.collect {
                _teachers.value = it
            }
        }
    }

    override fun deleteLesson(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { lessonRepo.deleteLesson(id) }
            _success.emit("Delete Lesson Successfully")
        }
    }

    override fun filterLessons(classSelect: String?, dateSelect: String?) {
        val filteredLessons = _lessons.value.filter { lesson ->
            (classSelect == null || lesson.classes == classSelect) &&
                    (dateSelect == null || lesson.date == dateSelect)
        }
        _filteredLessons.value = filteredLessons
    }
}