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
import com.renbin.student_attendance_app.databinding.FragmentTeacherHomeBinding
import com.renbin.student_attendance_app.ui.adapter.LessonAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.home.viewModel.TeacherHomeViewModelImpl
import com.renbin.student_attendance_app.ui.screens.tabContainer.TeacherTabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherHomeFragment : BaseFragment<FragmentTeacherHomeBinding>() {
    override val viewModel: TeacherHomeViewModelImpl by viewModels()
    private lateinit var lessonAdapter: LessonAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTeacherHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupLessonAdapter()

        binding.run {
            btnTeacherLogout.setOnClickListener {
                alertLogout()
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.success.collect{
                val action = TeacherTabContainerFragmentDirections.actionGlobalMain()
                navController.navigate(action)
            }
        }

        lifecycleScope.launch {
            viewModel.lessons.collect{
                lessonAdapter.setLessons(it)
            }
        }

        lifecycleScope.launch {
            viewModel.students.collect{
                lessonAdapter.setStudents(it)
            }
        }

        lifecycleScope.launch {
            viewModel.teachers.collect{
                lessonAdapter.setTeachers(it)
            }
        }

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

    private fun setupLessonAdapter(){
        lessonAdapter = LessonAdapter(emptyList(), emptyList(), emptyList(), viewModel.user, "Home")
        binding.rvLesson.adapter = lessonAdapter
        binding.rvLesson.layoutManager = LinearLayoutManager(requireContext())
    }

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
            viewModel.logout()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}