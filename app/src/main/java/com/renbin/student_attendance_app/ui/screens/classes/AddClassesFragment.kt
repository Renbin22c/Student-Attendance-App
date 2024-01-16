package com.renbin.student_attendance_app.ui.screens.classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.renbin.student_attendance_app.databinding.FragmentAddClassesBinding
import com.renbin.student_attendance_app.ui.screens.classes.viewModel.AddClassesViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Dagger Hilt AndroidEntryPoint annotation for dependency injection
@AndroidEntryPoint
class AddClassesFragment : DialogFragment() {
    // View model instance injected using the by viewModels() property delegate
    private val viewModel: AddClassesViewModelImpl by viewModels()
    // View binding for the fragment layout
    private lateinit var binding: FragmentAddClassesBinding

    // Function to create the fragment's view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        binding = FragmentAddClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Function called after the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up click listeners and actions for UI elements using view binding
        binding.run {
            ivClose.setOnClickListener { dismiss() }

            btnAdd.setOnClickListener {
                // Get the class name from the input field and call the ViewModel's addClasses function
                val name = etClasses.text.toString()
                viewModel.addClasses(name)
            }
        }

        // Use lifecycleScope to collect success events from the ViewModel
        lifecycleScope.launch {
            viewModel.success.collect {
                // Dismiss the dialog fragment when a success event is received
                dismiss()
            }
        }
    }
}