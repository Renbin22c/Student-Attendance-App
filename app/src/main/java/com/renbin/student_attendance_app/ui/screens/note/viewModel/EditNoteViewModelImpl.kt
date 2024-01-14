package com.renbin.student_attendance_app.ui.screens.note.viewModel

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
    override val classesName: StateFlow<List<String>> =_classesName

    private val _currentNote : MutableStateFlow<Note> = MutableStateFlow(Note(classes = "", title = "", desc = "", student = emptyList(), createdBy = ""))
    val currentNote: StateFlow<Note?> = _currentNote

    override fun onCreate() {
        super.onCreate()
        getAllClassesName()
    }


//    override fun setCurrentNoteId(noteId: String) {
//        viewModelScope.launch {
//            // Fetch the note details using the repository or your data source
//            val note = noteRepo.getNote(noteId)
//            _currentNote.value = note
//
//        }
//    }

    override fun getAllClassesName() {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                classesRepo.getAllClassesName()
            }?.collect{
                _classesName.value = it
            }
        }
    }
//    private var currentNoteId: String? = null
//
//    override fun editNotes(noteId: String,title: String, desc: String, classes: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val user = authService.getCurrentUser()
//
//            val students = studentRepo.getAllStudentByClass(classes)
//                .mapNotNull { student -> student.id }
//
//            user?.let {
//                val note = Note(
//                    title = title,
//                    desc = desc,
//                    classes = classes,
//                    student = students,
//                    createdBy = it.uid
//                )
//
//                // Pass the appropriate note ID as the first parameter
//                errorHandler { noteRepo.editNote(noteId, note) }
//
//                _success.emit("Update Note Successfully")
//            }
//        }
//    }

//    override fun submit(title: String, desc: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            errorHandler{ noteRepo.editNote(Note.value.id, Note.value.copy(title = title, desc = desc)) }
//            _success.emit(Unit.toString())
//        }
//    }

    fun getNote(id:String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler {
                noteRepo.getNote(id)
            }?.let {
                _currentNote.value = it
            }
        }
    }

    override fun submit(title: String, desc: String) {
        viewModelScope.launch(Dispatchers.IO) {
            errorHandler{ currentNote.value?.let { noteRepo.editNote(currentNote.value?.id.toString(), it.copy(title = title, desc = desc)) } }
            _success.emit(Unit.toString())
        }
    }
}