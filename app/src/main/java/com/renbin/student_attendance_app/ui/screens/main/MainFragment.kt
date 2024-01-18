package com.renbin.student_attendance_app.ui.screens.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.renbin.student_attendance_app.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

// Hilt AndroidEntryPoint annotation for dependency injection
@AndroidEntryPoint
class MainFragment : Fragment() {
    // Binding instance for the fragment's layout
    private lateinit var binding: FragmentMainBinding

    // NavController instance for navigating between destinations
    private lateinit var navController: NavController

    // Function to create the fragment's view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Function called after the fragment's view has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtain the NavController associated with this fragment
        navController = NavHostFragment.findNavController(this)

        // Set up click listeners for the buttons in the layout
        binding.run {
            // Button to navigate to the global student registration destination
            btnRegisterStudent.setOnClickListener {
                val action = MainFragmentDirections.actionGlobalStudentRegister()
                navController.navigate(action)
            }

            // Button to navigate to the global teacher registration destination
            btnRegisterTeacher.setOnClickListener {
                val action = MainFragmentDirections.actionGlobalTeacherRegister()
                navController.navigate(action)
            }

            // Button to navigate to the global login destination
            btnLoginStudent.setOnClickListener {
                val action = MainFragmentDirections.actionGlobalLogin()
                navController.navigate(action)
            }
        }
    }
}