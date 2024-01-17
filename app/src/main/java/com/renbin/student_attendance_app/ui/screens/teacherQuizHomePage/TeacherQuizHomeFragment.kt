package com.renbin.student_attendance_app.ui.screens.teacherQuizHomePage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.data.model.Quiz
import com.renbin.student_attendance_app.databinding.FragmentTeacherQuizHomeBinding
import com.renbin.student_attendance_app.ui.adapter.QuizAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.tabContainer.TeacherTabContainerFragmentDirections
import com.renbin.student_attendance_app.ui.screens.teacherQuizHomePage.viewModel.TeacherQuizHomeViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherQuizHomeFragment : BaseFragment<FragmentTeacherQuizHomeBinding>() {

    override val viewModel: TeacherQuizHomeViewModelImpl by viewModels()

    private lateinit var adapter: QuizAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTeacherQuizHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getQuiz()
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupAdapter()

        binding.fabAdd.setOnClickListener {
            val action = TeacherTabContainerFragmentDirections.actionTeacherTabContainerFragmentToTeacherAddQuizFragment3()
            navController.navigate(action)
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.quiz.collect {
                adapter.setQuiz(it)
            }
        }
    }



    private fun setupAdapter() {
        adapter = QuizAdapter(emptyList())

        adapter.listener = object : QuizAdapter.Listener {

            override fun onDelete(quiz: Quiz) {
                viewModel.delete(quiz)
            }

        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvQuiz.adapter = adapter
        binding.rvQuiz.layoutManager = layoutManager
    }

}