package com.renbin.student_attendance_app.ui.screens.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.renbin.student_attendance_app.databinding.FragmentStudentRegisterBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.register.viewModel.StudentRegisterViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Hilt AndroidEntryPoint annotation for dependency injection
@AndroidEntryPoint
class StudentRegisterFragment : BaseFragment<FragmentStudentRegisterBinding>() {
    // View model initialization using Hilt
    override val viewModel: StudentRegisterViewModelImpl by viewModels()

    // Adapter for the classes dropdown menu
    private lateinit var classesAdapter: ArrayAdapter<String>

    // Variable to store the selected class
    private var classSelect = ""

    // Called to create the fragment's view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Setup UI components and event listeners
    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        // Initialize the classes adapter for the dropdown menu
        classesAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            emptyList()
        )

        binding.run {
            // Set up click listener for the student registration button
            btnStudentRegister.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val pass = etPassword.text.toString()
                val confirmPass = etConfirmPassword.text.toString()

                // Call the studentRegister method on the view model with the provided information
                viewModel.studentRegister(name, email, pass, confirmPass, classSelect)
            }

            // Add text change listener to the classes dropdown menu
            autoCompleteCategory.addTextChangedListener {
                classSelect = it.toString()
            }

            // Set up click listener to navigate to the login screen
            tvLogin.setOnClickListener {
                val action = StudentRegisterFragmentDirections.actionGlobalLogin()
                navController.navigate(action)
            }
        }
    }

    // Observe view model data changes
    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        // Observe changes in the list of classes and update the adapter for the dropdown menu
        lifecycleScope.launch {
            viewModel.classesName.collect {
                classesAdapter = if (it.isEmpty()) {
                    ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        emptyList()
                    )
                } else {
                    ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        it
                    )
                }
                binding.autoCompleteCategory.setAdapter(classesAdapter)
            }
        }

        // Observe success state and navigate to the login screen on successful registration
        lifecycleScope.launch {
            viewModel.success.collect {
                val action = StudentRegisterFragmentDirections.actionGlobalLogin()
                navController.navigate(action)
            }
        }
    }
}