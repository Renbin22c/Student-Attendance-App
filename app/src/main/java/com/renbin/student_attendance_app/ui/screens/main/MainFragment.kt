package com.renbin.student_attendance_app.ui.screens.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)

        binding.run {
            btnRegisterStudent.setOnClickListener {
                val action = MainFragmentDirections.actionGlobalStudentRegister()
                navController.navigate(action)
            }

            btnRegisterTeacher.setOnClickListener {
                val action = MainFragmentDirections.actionGlobalTeacherRegister()
                navController.navigate(action)
            }

            btnLoginStudent.setOnClickListener {
                val action = MainFragmentDirections.actionGlobalLogin()
                navController.navigate(action)
            }
        }
    }
}