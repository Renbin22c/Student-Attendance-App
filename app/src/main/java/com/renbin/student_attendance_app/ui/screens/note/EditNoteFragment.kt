package com.renbin.student_attendance_app.ui.screens.note

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.appcompat.R
import androidx.navigation.fragment.navArgs
import com.renbin.student_attendance_app.databinding.FragmentEditNoteBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.note.viewModel.EditNoteViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditNoteFragment : BaseFragment<FragmentEditNoteBinding>() {

    private val args: EditNoteFragmentArgs by navArgs()
    override val viewModel: EditNoteViewModelImpl by viewModels()
    private lateinit var classesAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve arguments from NoteDetailsFragment
        val noteId = args.noteId

        // Setup UI components
        setupUIComponents(noteId)

        // Handle the "Update" button click
        binding.btnUpdate.setOnClickListener {
            val title = binding.etNoteTitle.text.toString()
            val desc = binding.etNoteDesc.text.toString()
            val selectedClass = binding.autoCompleteClass.text.toString()

            viewModel.editNotes(noteId, title, desc, selectedClass)
        }

        // Observe the ViewModel's StateFlow properties
        setupViewModelObserver()
    }

    private fun setupUIComponents(noteId: String) {
        binding.ibBack.setOnClickListener {
            navController.popBackStack()
        }
        binding.etNoteTitle.setText(viewModel.currentNote.value?.title ?: "")
        binding.etNoteDesc.setText(viewModel.currentNote.value?.desc ?: "")
        binding.autoCompleteClass.setText(viewModel.currentNote.value?.classes ?: "")

        // Fetch note details based on noteId
        viewModel.getNote(noteId)
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()
        lifecycleScope.launch {
            viewModel.currentNote.collect { note ->
                // Update the UI with the note details
                note?.let {
                    binding.etNoteTitle.setText(it.title)
                    binding.etNoteDesc.setText(it.desc)
                    binding.autoCompleteClass.setText(it.classes)
                    if(viewModel.classesName.value.isNotEmpty())updateClassesDropDown(viewModel.classesName.value)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.classesName.collect {
                updateClassesDropDown(it)
            }
        }

        lifecycleScope.launch{
            viewModel.success.collect{
                navController.popBackStack()
            }
        }
    }

    private fun updateClassesDropDown(classes: List<String>) {
        val classesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            classes
        )
        binding.autoCompleteClass.setAdapter(classesAdapter)
    }
}


