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
    override val viewModel: StudentDetailsViewModelImpl by viewModels()
    private lateinit var adapter: StudentDetailsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStudentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        setupStudentDetailsAdapter()

        binding.run {
            ibBack.setOnClickListener {
                navController.popBackStack()
            }

            ibEdit.setOnClickListener {
                val action = StudentDetailsFragmentDirections.actionStudentDetailsFragmentToStudentDetailsEditFragment2()
                navController.navigate(action)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.students.collect{
                adapter.setStudentDetails(it)
                binding.studentCount.text = "${it.size} Person"
            }
        }
    }

    private fun setupStudentDetailsAdapter(){
        adapter = StudentDetailsAdapter(emptyList())

        binding.rvStudent.adapter = adapter
        binding.rvStudent.layoutManager = LinearLayoutManager(requireContext())
    }
}