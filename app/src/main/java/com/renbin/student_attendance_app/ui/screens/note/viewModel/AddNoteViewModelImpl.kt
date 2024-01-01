package com.renbin.student_attendance_app.ui.screens.note.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.data.model.Note
import com.renbin.student_attendance_app.data.repo.classes.ClassesRepo
import com.renbin.student_attendance_app.data.repo.notes.NoteRepo
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModelImpl @Inject constructor(
    private val noteRepo: NoteRepo,
    private val classesRepo: ClassesRepo,
    private val authService: AuthService
): BaseViewModel(), AddNoteViewModel {
    private val _classesName = MutableStateFlow<List<String>>(emptyList())
    override val classesName: StateFlow<List<String>> =_classesName

    private val user = authService.getCurrentUser()

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





    override fun addNotes(title: String, desc: String, classes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = authService.getCurrentUser()
            user?.let{
                errorHandler { noteRepo.addNote(Note(title = title, desc = desc, classes = classes, createdBy = it.uid)) }
                _success.emit("Add Note Successfully")
            }
        }
    }

}