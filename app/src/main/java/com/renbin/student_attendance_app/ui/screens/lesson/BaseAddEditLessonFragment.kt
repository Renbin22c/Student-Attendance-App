package com.renbin.student_attendance_app.ui.screens.lesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentBaseAddEditLessonBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.base.viewModel.BaseViewModel
import com.renbin.student_attendance_app.ui.screens.lesson.viewModel.BaseAddEditViewModelImpl

abstract class BaseAddEditLessonFragment : BaseFragment<FragmentBaseAddEditLessonBinding>() {
    abstract override val viewModel: BaseAddEditViewModelImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBaseAddEditLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

}