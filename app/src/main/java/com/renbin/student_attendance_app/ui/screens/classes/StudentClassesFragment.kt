package com.renbin.student_attendance_app.ui.screens.classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentStudentClassesBinding
import com.renbin.student_attendance_app.ui.adapter.StudentAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.classes.viewModel.StudentClassesViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Dagger Hilt AndroidEntryPoint annotation for dependency injection
@AndroidEntryPoint
class StudentClassesFragment : BaseFragment<FragmentStudentClassesBinding>() {
    // View model instance injected using the by viewModels() property delegate
    override val viewModel: StudentClassesViewModelImpl by viewModels()
    // Adapter for the student list RecyclerView
    private lateinit var adapter: StudentAdapter

    // Function to create the fragment's view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        binding = FragmentStudentClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Function to set up UI components
    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        // Set up the RecyclerView adapter
        setupStudentAdapter()
    }

    // Function to set up ViewModel observers
    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        // Observe changes in the current student's classes and fetch students for that class
        lifecycleScope.launch {
            viewModel.student.collect {
                viewModel.getAllStudentsByClass(it.classes)
                binding.className.text = it.classes
            }
        }

        // Observe changes in the list of students and update the adapter
        lifecycleScope.launch {
            viewModel.students.collect {
                adapter.setStudent(it)
                binding.studentCount.text = "${it.size} ${getString(R.string.person)}"
            }
        }

        // Observe loading state and update UI components accordingly
        lifecycleScope.launch {
            viewModel.loading.collect {
                if (it) {
                    binding.progressbar.visibility = View.VISIBLE
                } else {
                    binding.progressbar.visibility = View.GONE
                }
            }
        }
    }

    // Function to set up the student list RecyclerView adapter
    private fun setupStudentAdapter() {
        adapter = StudentAdapter(emptyList())

        // Set the adapter for the RecyclerView and use LinearLayoutManager
        binding.rvStudent.adapter = adapter
        binding.rvStudent.layoutManager = LinearLayoutManager(requireContext())
    }
}