package com.renbin.student_attendance_app.ui.screens.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentAddClassesBinding
import com.renbin.student_attendance_app.ui.screens.classes.viewModel.AddClassesViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddClassesFragment : DialogFragment() {
    private val viewModel: AddClassesViewModelImpl by viewModels()
    private lateinit var binding: FragmentAddClassesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            ivClose.setOnClickListener { dismiss() }

            btnAdd.setOnClickListener {
                val name = etClasses.text.toString()
                viewModel.addClasses(name)
            }
        }

        lifecycleScope.launch {
            viewModel.success.collect{
                dismiss()
            }
        }
    }
}