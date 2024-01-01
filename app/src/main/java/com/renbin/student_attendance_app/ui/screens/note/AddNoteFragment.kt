package com.renbin.student_attendance_app.ui.screens.note

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentAddNoteBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.note.viewModel.AddNoteViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNoteFragment : BaseFragment<FragmentAddNoteBinding>() {

    override val viewModel: AddNoteViewModelImpl by viewModels()
    private lateinit var classesAdapter: ArrayAdapter<String>
    private var classSelect = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        classesAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            emptyList()
        )

        binding.run {
            ibBack.setOnClickListener {
                navController.popBackStack()
            }

            autoCompleteClass.addTextChangedListener {
                classSelect = it.toString()
            }

            btnSubmit.setOnClickListener {
                val title = binding.etNoteTitle.text.toString()
                val desc = binding.etNoteDesc.text.toString()

                viewModel.addNotes(title, desc, classSelect)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.classesName.collect {
                classesAdapter = if(it.isEmpty()){
                    ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        emptyList()
                    )
                } else{
                    ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        it
                    )
                }
                binding.autoCompleteClass.setAdapter(classesAdapter)
            }
        }
        lifecycleScope.launch{
            viewModel.success.collect{
                navController.popBackStack()
            }
        }
    }
}