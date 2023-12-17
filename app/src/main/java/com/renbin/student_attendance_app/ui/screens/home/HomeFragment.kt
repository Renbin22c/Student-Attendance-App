package com.renbin.student_attendance_app.ui.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentHomeBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.home.viewModel.HomeViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val viewModel: HomeViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        binding.run {
            btnClasses.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeToClasses()
                navController.navigate(action)
            }

            btnLogout.setOnClickListener {
                viewModel.logout()
                val action = HomeFragmentDirections.actionGlobalLogin()
                navController.navigate(action)
            }
        }
    }
}