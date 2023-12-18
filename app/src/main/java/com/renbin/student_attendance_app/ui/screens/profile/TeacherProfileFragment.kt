package com.renbin.student_attendance_app.ui.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.renbin.student_attendance_app.databinding.FragmentTeacherProfileBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.profile.viewModel.TeacherProfileViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherProfileFragment : BaseFragment<FragmentTeacherProfileBinding>() {
    override val viewModel: TeacherProfileViewModelImpl by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTeacherProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

}