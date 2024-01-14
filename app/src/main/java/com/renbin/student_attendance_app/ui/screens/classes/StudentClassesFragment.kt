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

@AndroidEntryPoint
class StudentClassesFragment : BaseFragment<FragmentStudentClassesBinding>() {
    override val viewModel: StudentClassesViewModelImpl by viewModels()
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        setupStudentAdapter()
    }


    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.student.collect{
                viewModel.getAllStudentsByClass(it.classes)
                binding.className.text = it.classes
            }
        }

        lifecycleScope.launch {
            viewModel.students.collect{
                adapter.setStudent(it)
                binding.studentCount.text = "${it.size} ${getString(R.string.person)}"
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collect{
                if (it){
                    binding.progressbar.visibility = View.VISIBLE
                } else {
                    binding.progressbar.visibility = View.GONE
                }
            }
        }
    }

    private fun setupStudentAdapter(){
        adapter = StudentAdapter(emptyList())

        binding.rvStudent.adapter = adapter
        binding.rvStudent.layoutManager = LinearLayoutManager(requireContext())
    }
}