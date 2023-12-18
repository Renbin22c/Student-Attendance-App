package com.renbin.student_attendance_app.ui.screens.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentNoteBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import com.renbin.student_attendance_app.ui.screens.note.viewModel.NoteViewModelImpl

class NoteFragment : BaseFragment<FragmentNoteBinding>() {
    override val viewModel: NoteViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

}