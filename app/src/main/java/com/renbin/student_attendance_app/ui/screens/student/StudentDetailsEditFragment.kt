package com.renbin.student_attendance_app.ui.screens.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.databinding.FragmentStudentDetailsEditBinding
import com.renbin.student_attendance_app.ui.adapter.StudentDetailsEditAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.student.viewModel.StudentDetailsEditViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudentDetailsEditFragment : BaseFragment<FragmentStudentDetailsEditBinding>() {
    override val viewModel: StudentDetailsEditViewModelImpl by viewModels()
    private lateinit var adapter: StudentDetailsEditAdapter
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentDetailsEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupStudentDetailsEditAdapter()
        search()

        binding.run {
            ibCheck.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.students.collect{
                adapter.setStudentDetailsEdit(it)
            }
        }

        lifecycleScope.launch{
            viewModel.classesName.collect{
                adapter.setClass(it)
            }
        }
    }

    private fun setupStudentDetailsEditAdapter(){
        adapter = StudentDetailsEditAdapter(emptyList(), emptyList(), requireContext())
        adapter.listener = object: StudentDetailsEditAdapter.Listener{
            override fun onUpdate(student: Student, classes: String) {
                viewModel.updateStudent(student, classes)
            }

            override fun onDelete(student: Student) {
                TODO("Not yet implemented")
            }

        }
        binding.rvStudentDetailsEdit.adapter = adapter
        binding.rvStudentDetailsEdit.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun search(){
        searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null){
                    val filteredEmails = viewModel.filterEmailByQuery(query)
                    adapter.setStudentDetailsEdit(filteredEmails)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    val filteredEmails = viewModel.filterEmailByQuery(newText)
                    adapter.setStudentDetailsEdit(filteredEmails)
                }else{
                    adapter.setStudentDetailsEdit(emptyList())
                }
                return true
            }
        })
    }


}