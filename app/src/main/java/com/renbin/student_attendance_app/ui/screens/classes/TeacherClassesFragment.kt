package com.renbin.student_attendance_app.ui.screens.classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.data.model.Classes
import com.renbin.student_attendance_app.databinding.FragmentTeacherClassesBinding
import com.renbin.student_attendance_app.ui.adapter.TeacherClassesAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.classes.viewModel.TeacherClassesViewModelImpl
import com.renbin.student_attendance_app.ui.screens.tabContainer.TeacherTabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Dagger Hilt AndroidEntryPoint annotation for dependency injection
@AndroidEntryPoint
class TeacherClassesFragment : BaseFragment<FragmentTeacherClassesBinding>() {
    // View model instance injected using the by viewModels() property delegate
    override val viewModel: TeacherClassesViewModelImpl by viewModels()
    // Adapter for the teacher classes RecyclerView
    private lateinit var adapter: TeacherClassesAdapter

    // Function to create the fragment's view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        binding = FragmentTeacherClassesBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Function to set up UI components
    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        // Set up the RecyclerView adapter and click listeners
        setupTeacherClassesAdapter()

        // Set up click listeners for UI elements in the layout
        binding.run {
            btnAddClass.setOnClickListener {
                // Show the AddClassesFragment dialog when the "Add Class" button is clicked
                val dialogFragment = AddClassesFragment()
                dialogFragment.show(childFragmentManager, "AddClassesFragment")
            }

            cvStudent.setOnClickListener {
                // Navigate to the StudentDetails screen when the "Student" card is clicked
                val action = TeacherTabContainerFragmentDirections.actionTeacherTabContainerToStudentDetails()
                navController.navigate(action)
            }
        }
    }

    // Function to set up ViewModel observers
    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        // Observe changes in the list of classes and update the adapter
        lifecycleScope.launch {
            viewModel.classes.collect {
                adapter.setClasses(it.sortedBy { classes -> classes.name })
            }
        }

        // Observe changes in the list of teachers and update the adapter
        lifecycleScope.launch {
            viewModel.teachers.collect {
                adapter.setTeachers(it)
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

    // Function to set up the teacher classes RecyclerView adapter
    private fun setupTeacherClassesAdapter() {
        adapter = TeacherClassesAdapter(emptyList(), emptyList())
        adapter.listener = object : TeacherClassesAdapter.Listener {
            // Handle click on a class item
            override fun onClick(classes: Classes) {
                val action = TeacherTabContainerFragmentDirections.actionTeacherTabContainerToClassesDetails(classes.name)
                navController.navigate(action)
            }

            // Handle click on the delete button for a class
            override fun onDelete(classes: Classes) {
                // Check if the class has students before allowing deletion
                viewModel.checkClassStudents(classes.name)
                // Show the delete confirmation dialog
                alertDelete(classes.id!!)
            }
        }

        // Set the adapter for the RecyclerView and use StaggeredGridLayoutManager
        binding.rvClasses.adapter = adapter
        binding.rvClasses.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    }

    // Function to show the delete confirmation dialog
    private fun alertDelete(id: String) {
        val dialogView = layoutInflater.inflate(R.layout.alert_dialog, null)
        val alertDialog = MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Rounded)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tvTitle)
        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)

        // Set up dialog components
        tvTitle.text = getString(R.string.delete_confirmation)

        lifecycleScope.launch {
            viewModel.isStudentsEmpty.collect {
                if (it) {
                    tvMessage.text = getString(R.string.delete_class)
                    btnConfirm.visibility = View.VISIBLE
                } else {
                    tvMessage.text = getString(R.string.cannot_delete_class)
                    btnConfirm.visibility = View.GONE
                }
            }
        }

        // Set up click listeners for dialog buttons
        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            // Delete the class if confirmation is received
            viewModel.deleteClasses(id)
            alertDialog.dismiss()
        }

        // Show the delete confirmation dialog
        alertDialog.show()
    }
}