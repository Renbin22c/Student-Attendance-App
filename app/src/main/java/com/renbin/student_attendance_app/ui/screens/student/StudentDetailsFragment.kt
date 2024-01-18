package com.renbin.student_attendance_app.ui.screens.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.renbin.student_attendance_app.databinding.FragmentStudentDetailsBinding
import com.renbin.student_attendance_app.ui.adapter.StudentDetailsAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.student.viewModel.StudentDetailsViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudentDetailsFragment : BaseFragment<FragmentStudentDetailsBinding>() {
    // View model initialization using Hilt
    override val viewModel: StudentDetailsViewModelImpl by viewModels()
    // Adapter for displaying student details
    private lateinit var adapter: StudentDetailsAdapter

    // Called to create the fragment's view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStudentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Setup UI components and event listeners
    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        // Setup the adapter for displaying student details
        setupStudentDetailsAdapter()

        binding.run {
            // Navigate back when the back button is clicked
            ibBack.setOnClickListener {
                navController.popBackStack()
            }

            // Navigate to the edit fragment when the edit button is clicked
            ibEdit.setOnClickListener {
                val action = StudentDetailsFragmentDirections.actionStudentDetailsFragmentToStudentDetailsEditFragment2()
                navController.navigate(action)
            }
        }
    }

    // Observe view model data changes
    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        // Observe changes in the list of students and update the adapter
        lifecycleScope.launch {
            viewModel.students.collect {
                adapter.setStudentDetails(it)
                binding.studentCount.text = "${it.size} Person"
            }
        }

        // Observe loading state and update UI accordingly
        lifecycleScope.launch {
            viewModel.loading.collect {
                if (it) {
                    // Show progress bar while loading
                    binding.progressbar.visibility = View.VISIBLE
                } else {
                    // Hide progress bar when loading is complete
                    binding.progressbar.visibility = View.GONE

                    // Show empty view if there are no students
                    if (adapter.itemCount == 0) {
                        binding.tvEmpty.visibility = View.VISIBLE
                    } else {
                        binding.tvEmpty.visibility = View.GONE
                    }
                }
            }
        }
    }

    // Initialize and set up the adapter for displaying student details
    private fun setupStudentDetailsAdapter() {
        adapter = StudentDetailsAdapter(emptyList())

        binding.rvStudent.adapter = adapter
        binding.rvStudent.layoutManager = LinearLayoutManager(requireContext())
    }
}