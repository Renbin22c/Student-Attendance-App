package com.renbin.student_attendance_app.ui.screens.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.renbin.student_attendance_app.databinding.FragmentStudentLessonBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.lesson.viewModel.StudentStudentLessonViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentLessonFragment : BaseFragment<FragmentStudentLessonBinding>() {
    override val viewModel: StudentStudentLessonViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }
}