package com.renbin.student_attendance_app.ui.screens.classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentClassesDetailsBinding
import com.renbin.student_attendance_app.ui.adapter.StudentAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.classes.viewModel.ClassesDetailsViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Dagger Hilt AndroidEntryPoint annotation for dependency injection
@AndroidEntryPoint
class ClassesDetailsFragment : BaseFragment<FragmentClassesDetailsBinding>() {

    // View model instance injected using the by viewModels() property delegate
    override val viewModel: ClassesDetailsViewModelImpl by viewModels()
    // Navigation arguments for retrieving class name
    private val args: ClassesDetailsFragmentArgs by navArgs()
    // Adapter for the student list RecyclerView
    private lateinit var adapter: StudentAdapter

    // Function to create the fragment's view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        binding = FragmentClassesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Function to set up UI components
    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        // Set up the RecyclerView adapter and fetch students for the class
        setupStudentAdapter()
        viewModel.getAllStudentsByClass(args.className)

        // Set the class name in the UI
        binding.className.text = args.className

        // Set up a click listener for the back button to navigate back
        binding.ibBack.setOnClickListener {
            navController.popBackStack()
        }
    }

    // Function to set up ViewModel observers
    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

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

                    // Show empty state message if the adapter is empty
                    if (adapter.itemCount == 0) {
                        binding.tvEmpty.visibility = View.VISIBLE
                    } else {
                        binding.tvEmpty.visibility = View.GONE
                    }
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