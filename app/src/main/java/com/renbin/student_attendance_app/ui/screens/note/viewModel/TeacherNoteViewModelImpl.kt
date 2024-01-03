package com.renbin.student_attendance_app.ui.screens.note.viewModel

import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.model.Note
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.data.repo.notes.NoteRepo
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
class TeacherNoteViewModelImpl @Inject constructor(
    private val noteRepo: NoteRepo,
    private val teacherRepo: TeacherRepo,
    authService: AuthService
): BaseViewModel(), TeacherNoteViewModel {
    private val _notes: MutableStateFlow<List<Note>> = MutableStateFlow(emptyList())
    override val notes: StateFlow<List<Note>> = _notes

    private val _teachers: MutableStateFlow<List<Teacher>> = MutableStateFlow(emptyList())
    override val teachers: StateFlow<List<Teacher>> = _teachers

    val user = authService.getCurrentUser()

    override fun onCreate() {
        super.onCreate()
        getAllNotes()
        getAllTeachers()
    }
    override fun getAllTeachers() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { teacherRepo.getAllTeachers() }?.collect {
                _teachers.value = it
            }
        }
    }

    override fun getAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler { noteRepo.getAllNotes() }?.collect {
                _notes.value = it
            }
        }
    }
}