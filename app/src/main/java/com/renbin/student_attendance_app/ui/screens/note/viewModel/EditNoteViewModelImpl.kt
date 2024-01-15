package com.renbin.student_attendance_app.ui.screens.note.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Note
import com.renbin.student_attendance_app.data.repo.classes.ClassesRepo
import com.renbin.student_attendance_app.data.repo.notes.NoteRepo
import com.renbin.student_attendance_app.data.repo.student.StudentRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNoteViewModelImpl @Inject constructor(
    private val noteRepo: NoteRepo,
    private val classesRepo: ClassesRepo,
    private val authService: AuthService,
    private val studentRepo: StudentRepo
) : BaseViewModel(), EditNoteViewModel {

    private val _classesName = MutableStateFlow<List<String>>(emptyList())
    override val classesName: StateFlow<List<String>> = _classesName


    private val _currentNote: MutableStateFlow<Note?> = MutableStateFlow(null)
    val currentNote: StateFlow<Note?> = _currentNote

    override fun onCreate() {
        super.onCreate()
        getAllClassesName()
    }


    override fun getAllClassesName() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                classesRepo.getAllClassesName()
            }?.collect {
                _classesName.value = it
            }
        }
    }

    override fun getNote(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                noteRepo.getNote(id)
            }?.let {
                Log.d("EditNoteViewModel", "Note retrieved: $it")
                _currentNote.value = it
            }
        }
    }

    override fun editNotes(noteId: String, title: String, desc: String, classes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = authService.getCurrentUser()

            val students = studentRepo.getAllStudentByClass(classes)
                .mapNotNull { student -> student.id }

            user?.let {
                val updatedNote = currentNote.value?.copy(
                    title = title,
                    desc = desc,
                    classes = classes,
                    student = students,
                    createdBy = it.uid
                )

                updatedNote?.let {
                    errorHandler { noteRepo.editNote(noteId, it) }
                    _success.emit("Update Note Successfully")
                }
            }
        }
    }
}