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
import com.google.firebase.auth.FirebaseUser
import com.renbin.student_attendance_app.core.service.AuthService
import com.renbin.student_attendance_app.core.service.AuthServiceImpl
import com.renbin.student_attendance_app.data.model.Teacher
import com.renbin.student_attendance_app.databinding.FragmentNoteDetailsBinding
import com.renbin.student_attendance_app.ui.adapter.NoteAdapter
import com.renbin.student_attendance_app.ui.adapter.StudentAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.classes.ClassesDetailsFragmentArgs
import com.renbin.student_attendance_app.ui.screens.note.viewModel.NoteDetailsViewModelImpl
import com.renbin.student_attendance_app.ui.screens.tabContainer.TeacherTabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class NoteDetailsFragment : BaseFragment<FragmentNoteDetailsBinding>() {

    override val viewModel: NoteDetailsViewModelImpl by viewModels()
    private val args: NoteDetailsFragmentArgs by navArgs()
    private lateinit var adapter: NoteAdapter
    private val authService: AuthService = AuthServiceImpl()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        binding.run {
            ibBack.setOnClickListener {
                navController.popBackStack()
            }
            ivEdit.setOnClickListener {
                val action =  NoteDetailsFragmentDirections.actionNoteDetailsFragmentToEditNoteFragment(noteId = args.noteId)
                navController.navigate(action)
            }
        }
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
                val currentUser = authService.getCurrentUser()
                if (currentUser?.uid == noteCreatedBy) {
                    binding.ivEdit.visibility = View.VISIBLE
                } else {
                    binding.ivEdit.visibility = View.GONE
                }
            }
        }
    }
}