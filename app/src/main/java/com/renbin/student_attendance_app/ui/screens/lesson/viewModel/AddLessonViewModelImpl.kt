package com.renbin.student_attendance_app.ui.screens.lesson.viewModel

import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddLessonViewModelImpl @Inject constructor(
    private val classesRepo: ClassesRepo,
    private val studentRepo: StudentRepo,
    private val lessonRepo: LessonRepo
): BaseViewModel(), AddLessonViewModel{
    private val _classesName = MutableStateFlow<List<String>>(emptyList())
    override val classesName: StateFlow<List<String>> =_classesName

    private val _studentsId = MutableStateFlow<List<Student>>(emptyList())
    override val studentsId: StateFlow<List<Student>> = _studentsId

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

    override fun getAllStudentIdByClasses(classes: String) {
        viewModelScope.launch {
            errorHandler {
                studentRepo.getAllStudentByClass(classes)
            }?.collect{
                _studentsId.value = it
            }
        }
    }

    override fun addLesson(
        name: String,
        details: String,
        classes: String,
        date: String,
        time: String,
        studentId: List<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                lessonRepo.addLesson(
                    Lesson(
                        name = name, details = details, time = time,
                        date = date, classes = classes, student = studentId,
                        attendanceTime = List(studentId.size){"---"},
                        attendance = List(studentId.size){false})
                )
            }
            _success.emit("Add Lesson Successfully")
        }
    }

}