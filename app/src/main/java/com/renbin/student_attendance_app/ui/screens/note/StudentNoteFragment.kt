package com.renbin.student_attendance_app.ui.screens.note

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.renbin.student_attendance_app.data.model.Note
import com.renbin.student_attendance_app.databinding.FragmentStudentNoteBinding
import com.renbin.student_attendance_app.ui.adapter.NoteAdapter
import com.renbin.student_attendance_app.ui.adapter.StudentNoteAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.note.viewModel.StudentNoteViewModelImpl
import com.renbin.student_attendance_app.ui.screens.tabContainer.StudentTabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudentNoteFragment : BaseFragment<FragmentStudentNoteBinding>() {

    override val viewModel: StudentNoteViewModelImpl by viewModels()
    private lateinit var adapter: StudentNoteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentStudentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupNoteAdapter()
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.notes.collect{
                adapter.setNotes(it)
            }
        }
        lifecycleScope.launch {
            viewModel.teachers.collect{
                adapter.setTeachers(it)
            }
        }
        lifecycleScope.launch {
            viewModel.students.collect{
                adapter.setStudents(it)
            }
        }
    }


    private fun setupNoteAdapter() {
        adapter = StudentNoteAdapter(emptyList(), emptyList(), emptyList(), viewModel.user)
        adapter.listener = object : StudentNoteAdapter.Listener{
            override fun onClick(note: Note) {
                val action = StudentTabContainerFragmentDirections.actionStudentTabContainerToStudentNoteDetails(
                    note.title,
                    note.desc,
                    note.createdBy
                )
                navController.navigate(action)
            }

        }
        binding.rvNotes.adapter = adapter
        binding.rvNotes.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    }
}