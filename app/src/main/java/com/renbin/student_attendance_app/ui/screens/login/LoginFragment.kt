package com.renbin.student_attendance_app.ui.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.renbin.student_attendance_app.databinding.FragmentLoginBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.login.viewModel.LoginViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val viewModel: LoginViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        binding.run {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val pass = etPassword.text.toString()

                viewModel.login(email, pass)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.success.collect{
                viewModel.getStudent()
                viewModel.getTeacher()
            }
        }

        lifecycleScope.launch {
            viewModel.teacher.collect{
                if(it.id != null){
                    val action = LoginFragmentDirections.actionLoginFragmentToTeacherTabContainerFragment()
                    navController.navigate(action)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.student.collect{
                if(it.id !=null){
                    val action = LoginFragmentDirections.actionLoginFragmentToStudentTabContainerFragment()
                    navController.navigate(action)
                }
            }
        }
    }

}