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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {
    override val viewModel: SplashViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        viewModel.getStudent()
        viewModel.getTeacher()
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        Handler(Looper.getMainLooper()).postDelayed({
            if(viewModel.auth != null){
                lifecycleScope.launch{
                    viewModel.student.collect{
                        if(it.id!=null){
                            val action = SplashFragmentDirections.actionSplashToStudentTabContainer()
                            navController.navigate(action)
                        }
                    }
                }

                lifecycleScope.launch{
                    viewModel.teacher.collect{
                        if (it.id != null){
                            val action = SplashFragmentDirections.actionSplashToTeacherTabContainer()
                            navController.navigate(action)
                        }
                    }
                }

            } else {
                val action = SplashFragmentDirections.actionGlobalMain()
                navController.navigate(action)
            }
        }, 2000)
    }
}