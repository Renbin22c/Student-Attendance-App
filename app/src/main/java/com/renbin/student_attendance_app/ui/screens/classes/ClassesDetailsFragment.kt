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

@AndroidEntryPoint
class ClassesDetailsFragment : BaseFragment<FragmentClassesDetailsBinding>() {
    override val viewModel: ClassesDetailsViewModelImpl by viewModels()
    private val args: ClassesDetailsFragmentArgs by navArgs()
    private lateinit var adapter: StudentAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentClassesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupStudentAdapter()

        viewModel.getAllStudentsByClass(args.className)

        binding.className.text = args.className

        binding.ibBack.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.students.collect{
                adapter.setStudent(it)
                binding.studentCount.text = "${it.size} ${getString(R.string.person)}"
            }
        }
    }

    private fun setupStudentAdapter(){
        adapter = StudentAdapter(emptyList())

        binding.rvStudent.adapter = adapter
        binding.rvStudent.layoutManager = LinearLayoutManager(requireContext())
    }
}