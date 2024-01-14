package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.core.util.Utility.formatTimestamp
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
class StudentLessonViewModelImpl @Inject constructor(
    private val lessonRepo: LessonRepo,
    private val studentRepo: StudentRepo,
    private val teacherRepo: TeacherRepo,
    authService: AuthService
):BaseViewModel(), StudentLessonViewModel, LessonViewModel {
    private val _lessons: MutableStateFlow<List<Lesson>> = MutableStateFlow(emptyList())
    override val lessons: StateFlow<List<Lesson>> = _lessons

    private val _students: MutableStateFlow<List<Student>> = MutableStateFlow(emptyList())
    override val students: StateFlow<List<Student>> = _students

    private val _teachers: MutableStateFlow<List<Teacher>> = MutableStateFlow(emptyList())
    override val teachers: StateFlow<List<Teacher>> = _teachers

    val user = authService.getCurrentUser()

    override fun onCreate() {
        super.onCreate()
        getAllLesson()
        getAllStudents()
        getAllTeachers()
    }

    override fun getAllLesson() {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.emit(true)
            errorHandler { lessonRepo.getAllLessons() }?.collect{
                val filterLesson = it.filter {lesson ->
                    lesson.student.contains(user?.uid)
                }
                _lessons.value = filterLesson
                _loading.emit(false)
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

    override fun updateAttendanceStatus(id: String, lesson: Lesson) {
        viewModelScope.launch(Dispatchers.IO) {
            if(user != null){
                val index = lesson.student.indexOf(user.uid)
                if(index != -1) {
                    val attend = lesson.attendance.toMutableList()
                    val attendTime = lesson.attendanceTime.toMutableList()

                    attend[index] = true
                    attendTime[index] = formatTimestamp(System.currentTimeMillis())

                    val newLesson = lesson.copy(attendance = attend, attendanceTime = attendTime)

                    errorHandler { lessonRepo.updateLesson(id,newLesson) }
                    _success.emit("Check In Successfully")
                }
            }
        }
    }
}