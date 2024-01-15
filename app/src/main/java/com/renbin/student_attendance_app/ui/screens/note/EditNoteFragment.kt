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
import com.renbin.student_attendance_app.databinding.FragmentEditNoteBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.note.viewModel.EditNoteViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditNoteFragment : BaseFragment<FragmentEditNoteBinding>() {

    private var noteId: String? = null
    override val  viewModel: EditNoteViewModelImpl by viewModels()
    private lateinit var classesAdapter: ArrayAdapter<String>
    private var selectedClass:String  = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }


//    override fun setupUIComponents(view: View) {
//        super.setupUIComponents(view)
//        classesAdapter = ArrayAdapter(
//            requireContext(),
//            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
//            emptyList()
//        )
//
//
//        binding.run {
//            ibBack.setOnClickListener {
//                navController.popBackStack()
//            }
//
//            btnUpdate.setOnClickListener {
//                val title = etNoteTitle.text.toString()
//                val desc = etNoteDesc.text.toString()
//
//                autoCompleteClass.addTextChangedListener {
//                    selectedClass = it.toString()
//                }
//
//                viewModel.editNotes(noteId.orEmpty(), title, desc, selectedClass)
//            }
//        }
//    }
//
//    override fun setupViewModelObserver() {
//        super.setupViewModelObserver()
//
//        lifecycleScope.launch {
//            viewModel.currentNote.collect { note ->
//                // Update the UI with the note details
//                note?.let {
//                    binding.etNoteTitle.setText(it.title)
//                    binding.etNoteDesc.setText(it.desc)
//                    selectedClass = it.classes
//                    binding.autoCompleteClass.setText(it.classes)
//                    Log.d("EditNoteFragment", "Selected Class: $selectedClass")
//                }
//            }
//        }
//
//
//            lifecycleScope.launch {
//            viewModel.classesName.collect {
//                updateClassesDropDown(it)
//            }
//        }
//    }
//
//    private fun updateClassesDropDown(classes: List<String>) {
//        classesAdapter = if (classes.isEmpty()) {
//            ArrayAdapter(
//                requireContext(),
//                R.layout.support_simple_spinner_dropdown_item,
//                emptyList()
//            )
//        } else {
//            ArrayAdapter(
//                requireContext(),
//                R.layout.support_simple_spinner_dropdown_item,
//                classes
//            )
//        }
//
//        binding.autoCompleteClass.setAdapter(classesAdapter)
//
//        // Assuming you have the selectedClass value, set it as the selected item in the dropdown
//        val selectedClassPosition = classesAdapter.getPosition(selectedClass)
//        if (selectedClassPosition != -1) {
//            binding.autoCompleteClass.setSelection(selectedClassPosition)
//        }
//    }

}