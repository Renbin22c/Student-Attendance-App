package com.renbin.student_attendance_app.ui.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.renbin.student_attendance_app.databinding.FragmentStudentProfileBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.profile.viewModel.StudentProfileViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentProfileFragment : BaseFragment<FragmentStudentProfileBinding>() {
    override val viewModel: StudentProfileViewModelImpl by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        // Inflate the layout for this fragment
        binding = FragmentStudentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

}