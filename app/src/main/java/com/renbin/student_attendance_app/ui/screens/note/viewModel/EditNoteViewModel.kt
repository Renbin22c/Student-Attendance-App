package com.renbin.student_attendance_app.ui.screens.note.viewModel

import kotlinx.coroutines.flow.StateFlow

interface EditNoteViewModel {
    val classesName: StateFlow<List<String>>

    fun getAllClassesName()

//    fun editNotes(noteId: String, title: String, desc: String, classes: String)

    fun submit(title: String, desc: String)
}