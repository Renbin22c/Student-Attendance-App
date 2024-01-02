package com.renbin.student_attendance_app.ui.screens.note

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentNoteDetailsBinding
import com.renbin.student_attendance_app.databinding.FragmentStudentNoteDetailsBinding
import com.renbin.student_attendance_app.ui.adapter.NoteAdapter
import com.renbin.student_attendance_app.ui.adapter.StudentNoteAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.note.viewModel.NoteDetailsViewModelImpl
import com.renbin.student_attendance_app.ui.screens.note.viewModel.StudentNoteDetailsViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudentNoteDetailsFragment : BaseFragment<FragmentStudentNoteDetailsBinding>() {

    override val viewModel: StudentNoteDetailsViewModelImpl by viewModels()
    private val args: StudentNoteDetailsFragmentArgs by navArgs()
    private lateinit var adapter: StudentNoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        binding.run {
            ibBack.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the details from arguments
        val noteTitle = args.noteTitle
        val noteDesc = args.noteDesc
        val noteCreatedBy = args.noteCreatedBy

        // Update your UI using the retrieved details
        binding.tvTitle.text = noteTitle
        binding.tvDesc.text = noteDesc

        // Observe the teachers StateFlow
        lifecycleScope.launch {
            viewModel.teachers.collect { teachers ->
                // Find the teacher with the matching ID
                val matchTeacher = teachers.find { it.id == noteCreatedBy }

                // Display the teacher's name if found
                binding.tvCreatedBy.text = matchTeacher?.name
            }
        }
    }
}