package com.renbin.student_attendance_app.ui.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.databinding.FragmentStudentHomeBinding
import com.renbin.student_attendance_app.ui.adapter.LessonAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.home.viewModel.StudentHomeViewModelImpl
import com.renbin.student_attendance_app.ui.screens.tabContainer.StudentTabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Dagger Hilt AndroidEntryPoint annotation
@AndroidEntryPoint
class StudentHomeFragment : BaseFragment<FragmentStudentHomeBinding>() {
    // Initialize ViewModel using Dagger Hilt
    override val viewModel: StudentHomeViewModelImpl by viewModels()

    // Initialize LessonAdapter
    private lateinit var lessonAdapter: LessonAdapter

    // Function to create the view for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Function to setup UI components
    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        // Setup LessonAdapter
        setupLessonAdapter()

        binding.run {
            // Set click listener for the logout button
            btnStudentLogout.setOnClickListener {
                // Display logout confirmation dialog
                alertLogout()
            }
        }
    }

    // Function to observe ViewModel data changes
    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        // Observe logout success event
        lifecycleScope.launch {
            viewModel.logoutSuccess.collect{
                // Navigate to the main screen upon successful logout
                val action = StudentTabContainerFragmentDirections.actionGlobalMain()
                navController.navigate(action)
            }
        }

        // Observe changes in the list of lessons and update the adapter
        lifecycleScope.launch {
            viewModel.lessons.collect{
                lessonAdapter.setLessons(it)
            }
        }

        // Observe changes in the list of students and update the adapter
        lifecycleScope.launch {
            viewModel.students.collect{
                lessonAdapter.setStudents(it)
            }
        }

        // Observe changes in the list of teachers and update the adapter
        lifecycleScope.launch {
            viewModel.teachers.collect{
                lessonAdapter.setTeachers(it)
            }
        }

        // Observe loading status and update UI components accordingly
        lifecycleScope.launch {
            viewModel.loading.collect{
                if (it){
                    binding.progressbar.visibility = View.VISIBLE
                } else {
                    binding.progressbar.visibility = View.GONE
                    if(lessonAdapter.itemCount == 0){
                        binding.tvEmpty.visibility = View.VISIBLE
                    } else {
                        binding.tvEmpty.visibility = View.GONE
                    }
                }
            }
        }
    }

    // Function to setup LessonAdapter
    private fun setupLessonAdapter(){
        // Initialize LessonAdapter with empty lists
        lessonAdapter = LessonAdapter(emptyList(), emptyList(), emptyList(), viewModel.user, "Home")
        lessonAdapter.listener = object : LessonAdapter.Listener{
            // Handle click event on a lesson
            override fun onClick(id: String, lesson: Lesson) {
                // Update attendance status when a lesson is clicked
                viewModel.updateAttendanceStatus(id, lesson)
            }

            // Handle delete event on a lesson (Not yet implemented)
            override fun onDelete(lesson: Lesson) {
                TODO("Not yet implemented")
            }
        }
        // Set LessonAdapter for the RecyclerView
        binding.rvLesson.adapter = lessonAdapter
        // Set LinearLayoutManager for the RecyclerView
        binding.rvLesson.layoutManager = LinearLayoutManager(requireContext())
    }

    // Function to display logout confirmation dialog
    private fun alertLogout(){
        val dialogView = layoutInflater.inflate(R.layout.alert_dialog, null)
        val alertDialog = MaterialAlertDialogBuilder(requireContext(), R.style.MaterialAlertDialog_Rounded)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            // Initiate the logout process when confirmed
            viewModel.logout()
            alertDialog.dismiss()
        }

        // Show the logout confirmation dialog
        alertDialog.show()
    }
}