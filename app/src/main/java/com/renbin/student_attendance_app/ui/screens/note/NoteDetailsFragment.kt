package com.renbin.student_attendance_app.ui.screens.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.databinding.FragmentNoteDetailsBinding
import com.renbin.student_attendance_app.ui.adapter.NoteAdapter
import com.renbin.student_attendance_app.ui.adapter.StudentAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.classes.ClassesDetailsFragmentArgs
import com.renbin.student_attendance_app.ui.screens.note.viewModel.NoteDetailsViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteDetailsFragment : BaseFragment<FragmentNoteDetailsBinding>() {

    override val viewModel: NoteDetailsViewModelImpl by viewModels()
    private val args: NoteDetailsFragmentArgs by navArgs()
    private lateinit var adapter: NoteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the details from arguments
        val noteTitle = args.noteTitle
        val noteDesc = args.noteDesc
        val noteClasses = args.noteClasses
        val noteCreatedBy = args.noteCreatedBy

        // Update your UI using the retrieved details
        binding.tvTitle.text = noteTitle
        binding.tvDesc.text = noteDesc
        binding.tvClassName.text = noteClasses

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