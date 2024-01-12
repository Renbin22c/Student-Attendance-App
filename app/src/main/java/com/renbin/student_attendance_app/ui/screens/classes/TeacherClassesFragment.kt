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

            cvStudent.setOnClickListener {
                val action = TeacherTabContainerFragmentDirections.actionTeacherTabContainerToStudentDetails()
                navController.navigate(action)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.classes.collect{
                adapter.setClasses(it.sortedBy { classes -> classes.name })
            }
        }

        lifecycleScope.launch {
            viewModel.teachers.collect{
                adapter.setTeachers(it)
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collect{
                if (it){
                    binding.progressbar.visibility = View.VISIBLE
                } else {
                    binding.progressbar.visibility = View.GONE
                    if(adapter.itemCount == 0){
                        binding.tvEmpty.visibility = View.VISIBLE
                    } else {
                        binding.tvEmpty.visibility = View.GONE
                    }
                }
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

            override fun onDelete(classes: Classes) {
                viewModel.checkClassStudents(classes.name)
                alertDelete(classes.id!!)
            }

        }

        binding.rvClasses.adapter = adapter
        binding.rvClasses.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    }

    private fun alertDelete(id: String){
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

        lifecycleScope.launch {
            viewModel.isStudentsEmpty.collect{
                if (it){
                    tvMessage.text = getString(R.string.delete_class)
                    btnConfirm.visibility = View.VISIBLE
                } else{
                    tvMessage.text = getString(R.string.cannot_delete_class)
                    btnConfirm.visibility = View.GONE
                }
            }
        }

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            viewModel.deleteClasses(id)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}