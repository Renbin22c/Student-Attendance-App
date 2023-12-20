package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherLessonViewModelImpl @Inject constructor(
    private val lessonRepo: LessonRepo,
    private val studentRepo: StudentRepo,
    private val teacherRepo: TeacherRepo
): BaseViewModel(), TeacherLessonViewModel {
    private val _lessons: MutableStateFlow<List<Lesson>> = MutableStateFlow(emptyList())
    override val lessons: StateFlow<List<Lesson>> = _lessons

    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    override val students: StateFlow<List<Student>> = _students

    private val _teachers: MutableStateFlow<List<Teacher>> = MutableStateFlow(emptyList())
    override val teachers: StateFlow<List<Teacher>> = _teachers

    override fun onCreate() {
        super.onCreate()
        getAllLesson()
        getAllStudents()
        getAllTeachers()
    }

    override fun getAllLesson() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { lessonRepo.getAllLessons() }?.collect{
                _lessons.value = it
            }
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
}