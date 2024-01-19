package com.renbin.student_attendance_app.ui.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentLoginBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.login.viewModel.LoginViewModelImpl
import com.renbin.student_attendance_app.ui.screens.splash.SplashFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
// Hilt AndroidEntryPoint annotation for dependency injection
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    // View model initialization using Hilt
    override val viewModel: LoginViewModelImpl by viewModels()
    var student = true
    var teacher = true

    // Called to create the fragment's view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Setup UI components and event listeners
    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        binding.run {
            // Set up login button click listener
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val pass = etPassword.text.toString()

                // Call login method on the view model
                viewModel.login(email, pass)
            }
            // Set up forgot password text view click listener
            tvForgotPassword.setOnClickListener {
                // Navigate to the ForgetPasswordFragment
                val action = LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment()
                navController.navigate(action)
            }
        }
    }

    // Observe view model data changes
    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        // Observe success state and trigger fetching of student and teacher information
        lifecycleScope.launch {
            viewModel.finish.collect {
                viewModel.getStudent()
                viewModel.getTeacher()

                delay(2000)
                if(student&&teacher) {
                    viewModel.checkUserAuthentication()
                }
            }
        }

        // Observe teacher state and navigate to TeacherTabContainerFragment if teacher information is present
        lifecycleScope.launch {
            viewModel.teacher.collect {
                if (it.id != null) {
                    teacher = false
                    val action = LoginFragmentDirections.actionLoginFragmentToTeacherTabContainerFragment()
                    navController.navigate(action)
                }
            }
        }

        // Observe student state and navigate to StudentTabContainerFragment if student information is present
        lifecycleScope.launch {
            viewModel.student.collect {
                if (it.id != null) {
                    student = false
                    val action = LoginFragmentDirections.actionLoginFragmentToStudentTabContainerFragment()
                    navController.navigate(action)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.checkError.collect{
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.error
                        )
                    ).show()
                val action = LoginFragmentDirections.actionGlobalMain()
                navController.navigate(action)
            }
        }
    }
}