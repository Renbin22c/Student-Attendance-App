package com.renbin.student_attendance_app.ui.screens.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.renbin.student_attendance_app.databinding.FragmentSplashBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.splash.viewModel.SplashViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Hilt AndroidEntryPoint annotation for dependency injection
@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    // ViewModel instance injected using Hilt
    override val viewModel: SplashViewModelImpl by viewModels()
    var student = true
    var teacher = true

    // Function to create the fragment's view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Function to set up UI components
    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        // Trigger the ViewModel to fetch Student and Teacher data
        viewModel.getStudent()
        viewModel.getTeacher()
    }

    // Function to set up ViewModel observer
    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        // Use Handler to delay navigation after a specified time (2000 milliseconds in this case)
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if the user is authenticated
            if(viewModel.auth != null){
                lifecycleScope.launch{

                    // Collect Student data and navigate to the appropriate destination
                    viewModel.student.collect{
                        if(it.id!=null){
                            student = false
                            val action = SplashFragmentDirections.actionSplashToStudentTabContainer()
                            navController.navigate(action)
                        }
                    }
                }

                lifecycleScope.launch{
                    // Collect Teacher data and navigate to the appropriate destination
                    viewModel.teacher.collect{
                        if (it.id != null){
                            teacher = false
                            val action = SplashFragmentDirections.actionSplashToTeacherTabContainer()
                            navController.navigate(action)
                        }
                    }
                }
                lifecycleScope.launch {
                    delay(2000)
                    if(student&&teacher) {
                        viewModel.checkUserAuthentication()
                        val action = SplashFragmentDirections.actionGlobalMain()
                        navController.navigate(action)
                    }
                }

            } else {
                // If not authenticated, navigate to the main destination
                val action = SplashFragmentDirections.actionGlobalMain()
                navController.navigate(action)
            }
        }, 2000)
    }
}