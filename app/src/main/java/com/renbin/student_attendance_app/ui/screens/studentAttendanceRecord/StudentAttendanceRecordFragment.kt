package com.renbin.student_attendance_app.ui.screens.studentAttendanceRecord

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentStudentAttendanceRecordBinding
import com.renbin.student_attendance_app.databinding.FragmentStudentProfileBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.profile.viewModel.StudentProfileViewModelImpl
import com.renbin.student_attendance_app.ui.screens.studentAttendanceRecord.viewModel.StudentAttendanceRecordViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentAttendanceRecordFragment : BaseFragment<FragmentStudentAttendanceRecordBinding>() {

    override val viewModel: StudentProfileViewModelImpl by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentAttendanceRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        binding.run {
            btnBackToProfile.setOnClickListener {
                val action = StudentAttendanceRecordFragmentDirections.actionStudentAttendanceRecordToStudentTabContainerFragment()
                navController.navigate(action)
            }
        }
    }


}