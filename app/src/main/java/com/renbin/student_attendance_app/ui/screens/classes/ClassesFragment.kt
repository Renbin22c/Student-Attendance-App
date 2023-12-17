package com.renbin.student_attendance_app.ui.screens.classes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentClassesBinding
import com.renbin.student_attendance_app.ui.adapter.ClassesAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.classes.viewModel.ClassesViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClassesFragment : BaseFragment<FragmentClassesBinding>() {
    override val viewModel: ClassesViewModelImpl by viewModels()
    private lateinit var adapter: ClassesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupClassesAdapter()

        binding.run {
            btnAddClass.setOnClickListener {
                val dialogFragment = AddClassesFragment()
                dialogFragment.show(childFragmentManager, "AddClassesFragment")
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.classes.collect{
                adapter.setClasses(it)
            }
        }
    }

    private fun setupClassesAdapter(){
        adapter = ClassesAdapter(emptyList())

        binding.rvClasses.adapter = adapter
        binding.rvClasses.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    }
}