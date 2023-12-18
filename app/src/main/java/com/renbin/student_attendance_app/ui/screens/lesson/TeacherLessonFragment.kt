package com.renbin.student_attendance_app.ui.screens.lesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentTeacherLessonBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import com.renbin.student_attendance_app.ui.screens.lesson.viewModel.TeacherLessonViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherLessonFragment : BaseFragment<FragmentTeacherLessonBinding>() {
    override val viewModel: TeacherLessonViewModelImpl by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTeacherLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

}