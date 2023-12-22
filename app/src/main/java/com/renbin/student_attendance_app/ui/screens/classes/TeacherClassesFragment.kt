package com.renbin.student_attendance_app.ui.screens.classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.databinding.FragmentTeacherClassesBinding
import com.renbin.student_attendance_app.ui.adapter.TeacherClassesAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.classes.viewModel.TeacherClassesViewModelImpl
import com.renbin.student_attendance_app.ui.screens.tabContainer.TeacherTabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherClassesFragment : BaseFragment<FragmentTeacherClassesBinding>() {
    override val viewModel: TeacherClassesViewModelImpl by viewModels()
    private lateinit var adapter: TeacherClassesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTeacherClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupTeacherClassesAdapter()

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

        lifecycleScope.launch {
            viewModel.teachers.collect{
                adapter.setTeachers(it)
            }
        }

    }

    private fun setupTeacherClassesAdapter(){
        adapter = TeacherClassesAdapter(emptyList(), emptyList())
        adapter.listener = object :TeacherClassesAdapter.Listener{
            override fun onClick(classes: Classes) {
                val action = TeacherTabContainerFragmentDirections.actionTeacherTabContainerToClassesDetails(classes.name)
                navController.navigate(action)
            }

        }

        binding.rvClasses.adapter = adapter
        binding.rvClasses.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    }
}