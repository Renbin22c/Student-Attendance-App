package com.renbin.student_attendance_app.ui.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.renbin.student_attendance_app.databinding.FragmentStudentHomeBinding
import com.renbin.student_attendance_app.ui.adapter.LessonAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.home.viewModel.StudentHomeViewModelImpl
import com.renbin.student_attendance_app.ui.screens.tabContainer.StudentTabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudentHomeFragment : BaseFragment<FragmentStudentHomeBinding>() {
    override val viewModel: StudentHomeViewModelImpl by viewModels()
    private lateinit var lessonAdapter: LessonAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupLessonAdapter()

        binding.run {
            btnStudentLogout.setOnClickListener {
                viewModel.logout()
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.success.collect{
                val action = StudentTabContainerFragmentDirections.actionGlobalMain()
                navController.navigate(action)
            }
        }

        lifecycleScope.launch {
            viewModel.lessons.collect{
                lessonAdapter.setLessons(it)
                if (it.isEmpty()){
                    binding.tvEmpty.visibility = View.VISIBLE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            viewModel.students.collect{
                lessonAdapter.setStudents(it)
            }
        }

        lifecycleScope.launch {
            viewModel.teachers.collect{
                lessonAdapter.setTeachers(it)
            }
        }
    }

    private fun setupLessonAdapter(){
        lessonAdapter = LessonAdapter(emptyList(), emptyList(), emptyList(), viewModel.user)
        binding.rvLesson.adapter = lessonAdapter
        binding.rvLesson.layoutManager = LinearLayoutManager(requireContext())
    }

}