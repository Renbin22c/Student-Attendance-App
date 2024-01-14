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

@AndroidEntryPoint
class TeacherRegisterFragment : BaseFragment<FragmentTeacherRegisterBinding>() {
    override val viewModel: TeacherRegisterViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTeacherRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        binding.run {
            btnTeacherRegister.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val pass = etPassword.text.toString()
                val confirmPass = etConfirmPassword.text.toString()

                viewModel.teacherRegister(name, email, pass, confirmPass)
            }

            tvLogin.setOnClickListener {
                val action = TeacherRegisterFragmentDirections.actionGlobalLogin()
                navController.navigate(action)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch{
            viewModel.success.collect{
                val action = TeacherRegisterFragmentDirections.actionGlobalLogin()
                navController.navigate(action)
            }
        }
    }
}