package com.renbin.student_attendance_app.ui.screens.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.renbin.student_attendance_app.databinding.FragmentTeacherRegisterBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.register.viewModel.TeacherRegisterViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Hilt AndroidEntryPoint annotation for dependency injection
@AndroidEntryPoint
class TeacherRegisterFragment : BaseFragment<FragmentTeacherRegisterBinding>() {
    // View model initialization using Hilt
    override val viewModel: TeacherRegisterViewModelImpl by viewModels()

    // Called to create the fragment's view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTeacherRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Setup UI components and event listeners
    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        binding.run {
            // Set up click listener for the teacher registration button
            btnTeacherRegister.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val pass = etPassword.text.toString()
                val confirmPass = etConfirmPassword.text.toString()

                // Call the teacherRegister method on the view model with the provided information
                viewModel.teacherRegister(name, email, pass, confirmPass)
            }

            // Set up click listener to navigate to the login screen
            tvLogin.setOnClickListener {
                val action = TeacherRegisterFragmentDirections.actionGlobalLogin()
                navController.navigate(action)
            }
        }
    }

    // Observe view model data changes
    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        // Observe success state and navigate to the login screen on successful registration
        lifecycleScope.launch {
            viewModel.success.collect {
                val action = TeacherRegisterFragmentDirections.actionGlobalLogin()
                navController.navigate(action)
            }
        }
    }
}