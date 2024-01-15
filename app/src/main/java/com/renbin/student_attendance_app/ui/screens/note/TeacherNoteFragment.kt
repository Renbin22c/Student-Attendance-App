package com.renbin.student_attendance_app.ui.screens.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.renbin.student_attendance_app.data.model.Note
import com.renbin.student_attendance_app.databinding.FragmentTeacherNoteBinding
import com.renbin.student_attendance_app.ui.adapter.NoteAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.note.viewModel.TeacherNoteViewModelImpl
import com.renbin.student_attendance_app.ui.screens.tabContainer.TeacherTabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherNoteFragment : BaseFragment<FragmentTeacherNoteBinding>() {
    override val viewModel: TeacherNoteViewModelImpl by viewModels()
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTeacherNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupNoteAdapter()

        binding.run {
            btnAddNote.setOnClickListener {
                val action = TeacherTabContainerFragmentDirections.actionTeacherTabContainerToAddNote()
                navController.navigate(action)
            }
        }
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
    }

    private fun setupNoteAdapter() {
        adapter = NoteAdapter(emptyList(), emptyList(), viewModel.user)
        adapter.listener = object :NoteAdapter.Listener{
            override fun onClick(note: Note) {
                val action = TeacherTabContainerFragmentDirections.actionTeacherTabContainerFragmentToNoteDetailsFragment(
                    noteId = note.id.toString(),
                    note.title,
                    note.desc,
                    note.classes,
                    note.createdBy
                )
                navController.navigate(action)
            }

            override fun onDelete(note: Note) {
                viewModel.deleteNotes(note.id.toString())
            }
        }
        binding.rvNotes.adapter = adapter
        binding.rvNotes.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    }
}


