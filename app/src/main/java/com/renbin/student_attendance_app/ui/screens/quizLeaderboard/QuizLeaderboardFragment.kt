package com.renbin.student_attendance_app.ui.screens.quizLeaderboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentQuizLeaderboardBinding
import com.renbin.student_attendance_app.ui.adapter.ResultAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.quizLeaderboard.viewModel.QuizLeaderboardViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class QuizLeaderboardFragment : BaseFragment<FragmentQuizLeaderboardBinding>() {

    override val viewModel: QuizLeaderboardViewModelImpl by viewModels()

    private lateinit var adapter: ResultAdapter

    // Adapter for the category dropdown menu
    private lateinit var categoryAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizLeaderboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        // Set up the ResultAdapter for the RecyclerView
        setupAdapter()

        // Set up the ArrayAdapter for the category dropdown
        categoryAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            emptyArray()
        )

        binding.run {
            // Handle item selection in the category dropdown
            autoCompleteCategory.setOnItemClickListener { _, _, position, _ ->
                val selectedQuizId = categoryAdapter.getItem(position)
                if (!selectedQuizId.isNullOrBlank()) {
                    // Fetch results for the selected quiz
                    viewModel.getResultByQuizId(selectedQuizId)
                    categoryAdapter.clear()
                }
            }

            // Handle navigation back to the home screen
            fabBack.setOnClickListener {
                val action = QuizLeaderboardFragmentDirections.actionQuizLeaderboardFragmentToStudentTabContainerFragment()
                navController.navigate(action)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            // Observe the LiveData or StateFlow containing leaderboard results
            viewModel.score.collect { results ->
                // Update the ResultAdapter with the filtered and sorted list of results
                adapter.setResult(results.sortedByDescending { it.result.toIntOrNull() ?:0 })

                // Extract unique quizIds from the results
                val quizIds = results.map { it.quizId }.distinct()

                withContext(Dispatchers.Main) {
                    // Create a new list and update the categoryAdapter
                    val newQuizIds = ArrayList<String>(quizIds)
                    categoryAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        newQuizIds
                    )
                    binding.autoCompleteCategory.setAdapter(categoryAdapter)
                }
            }
        }
    }

    // Set up the ResultAdapter for the RecyclerView
    private fun setupAdapter() {
        adapter = ResultAdapter(emptyList())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvLeaderBoard.adapter = adapter
        binding.rvLeaderBoard.layoutManager = layoutManager
    }

}