package com.renbin.student_attendance_app.ui.screens.studentJoinQuizPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentStudentJoinQuizPageBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.studentJoinQuizPage.viewModel.StudentJoinQuizPageViewModel
import com.renbin.student_attendance_app.ui.screens.studentJoinQuizPage.viewModel.StudentJoinQuizPageViewModelImpl
import com.renbin.student_attendance_app.ui.screens.tabContainer.StudentTabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentJoinQuizPageFragment : BaseFragment<FragmentStudentJoinQuizPageBinding>() {

    override val viewModel: StudentJoinQuizPageViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentJoinQuizPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        binding.btnStart.setOnClickListener {
            val id = binding.etQuizId.text.toString()
            if (id.isEmpty()) {
                Snackbar.make(
                    requireView(),
                    "Quiz ID cannot be empty",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                viewModel.checkIfQuizExists(id, object : StudentJoinQuizPageViewModel.QuizExistsCallback {
                    override fun onQuizExists() {
                        // Quiz exists, navigate to QuizPageFragment
                        val action = StudentTabContainerFragmentDirections
                            .actionStudentTabContainerFragmentToQuizPageFragment(id)

                        // Clear the back stack before navigating
                        navController.popBackStack(R.id.studentJoinQuizPageFragment, false)

                        navController.navigate(action)
                    }

                    override fun onQuizDoesNotExist() {
                        Snackbar.make(
                            requireView(),
                            "Quiz with ID $id does not exist",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                })
            }
        }

        }

}



