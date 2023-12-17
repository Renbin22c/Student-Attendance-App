package com.renbin.student_attendance_app.ui.screens.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentStudentRegisterBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.register.viewModel.StudentRegisterViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentRegisterFragment : BaseFragment<FragmentStudentRegisterBinding>() {
    override val viewModel: StudentRegisterViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        binding.run {
            btnStudentRegister.setOnClickListener {
                val action = StudentRegisterFragmentDirections.actionGlobalLogin()
                navController.navigate(action)
            }
        }
    }
}