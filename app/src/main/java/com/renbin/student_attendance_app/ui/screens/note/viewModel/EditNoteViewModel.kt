package com.renbin.student_attendance_app.ui.screens.note.viewModel

import com.renbin.student_attendance_app.data.model.Note
import kotlinx.coroutines.flow.StateFlow

interface EditNoteViewModel {
    val classesName: StateFlow<List<String>>

    fun getAllClassesName()

    fun getNote(id: String)

    fun editNotes(noteId: String, title: String, desc: String, classes: String)
}