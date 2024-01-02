package com.renbin.student_attendance_app.ui.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.renbin.student_attendance_app.databinding.FragmentStudentProfileBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.profile.viewModel.StudentProfileViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudentProfileFragment : BaseFragment<FragmentStudentProfileBinding>() {
    override val viewModel: StudentProfileViewModelImpl by viewModels()
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        // Inflate the layout for this fragment
        binding = FragmentStudentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.user.collect {
                binding.tvUserName.text = it.name
                binding.tvUserEmail.text = it.email
                binding.tvUserClass.text = it.classes
            }
        }
    }

}