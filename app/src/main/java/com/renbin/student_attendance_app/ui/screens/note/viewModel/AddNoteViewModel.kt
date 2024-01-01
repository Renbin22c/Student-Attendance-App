package com.renbin.student_attendance_app.ui.screens.note.viewModel

import kotlinx.coroutines.flow.StateFlow

interface AddNoteViewModel {
    val classesName: StateFlow<List<String>>

    fun getAllClassesName()

    fun addNotes(title: String, desc: String, classes: String)
}