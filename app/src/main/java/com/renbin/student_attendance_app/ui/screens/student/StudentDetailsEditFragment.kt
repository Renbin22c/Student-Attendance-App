package com.renbin.student_attendance_app.ui.screens.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.data.model.Student
import com.renbin.student_attendance_app.databinding.FragmentStudentDetailsEditBinding
import com.renbin.student_attendance_app.ui.adapter.StudentDetailsEditAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.student.viewModel.StudentDetailsEditViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudentDetailsEditFragment : BaseFragment<FragmentStudentDetailsEditBinding>() {
    // Initialize ViewModel using Dagger Hilt
    override val viewModel: StudentDetailsEditViewModelImpl by viewModels()
    // Initialize LessonAdapter
    private lateinit var adapter: StudentDetailsEditAdapter
    // Hold the SearchView
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
        // Initialize and set up the adapter and search functionality
        setupStudentDetailsEditAdapter()
        search()

        binding.run {
            ibCheck.setOnClickListener {
                // Navigate back when the check button is clicked
                navController.popBackStack()
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        // Observe changes in student list and update the adapter
        lifecycleScope.launch {
            viewModel.students.collect {
                adapter.setStudentDetailsEdit(it)
            }
        }

        // Observe changes in class names and update the adapter
        lifecycleScope.launch {
            viewModel.classesName.collect {
                adapter.setClass(it)
            }
        }

        // Observe loading state and show/hide progress bar accordingly
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

    private fun setupStudentDetailsEditAdapter() {
        // Initialize the adapter and set up its listeners
        adapter = StudentDetailsEditAdapter(emptyList(), emptyList(), requireContext())
        adapter.listener = object : StudentDetailsEditAdapter.Listener {
            override fun onUpdate(student: Student, classes: String) {
                // Handle student update
                viewModel.updateStudent(student, classes)
            }

            override fun onDelete(student: Student) {
                // Show delete confirmation dialog
                alertDelete(student.id!!)
            }
        }
        binding.rvStudentDetailsEdit.adapter = adapter
        binding.rvStudentDetailsEdit.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun search() {
        // Set up search functionality using the SearchView
        searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Filter and update adapter on text submit
                if (query != null) {
                    val filteredEmails = viewModel.filterEmailByQuery(query)
                    adapter.setStudentDetailsEdit(filteredEmails)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter and update adapter on text change
                if (newText != null) {
                    val filteredEmails = viewModel.filterEmailByQuery(newText)
                    adapter.setStudentDetailsEdit(filteredEmails)
                } else {
                    adapter.setStudentDetailsEdit(emptyList())
                }
                return true
            }
        })
    }

    private fun alertDelete(id: String) {
        // Show delete confirmation dialog
        val dialogView = layoutInflater.inflate(R.layout.alert_dialog, null)
        val alertDialog = MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Rounded)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)

        tvTitle.text = getString(R.string.delete_confirmation)
        tvMessage.text = getString(R.string.delete_student)

        btnCancel.setOnClickListener {
            // Dismiss the dialog on cancel button click
            alertDialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            // Delete the student on confirm button click
            viewModel.deleteStudent(id)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}