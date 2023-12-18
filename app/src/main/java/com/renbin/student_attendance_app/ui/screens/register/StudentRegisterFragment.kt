package com.renbin.student_attendance_app.ui.screens.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.renbin.student_attendance_app.databinding.FragmentStudentRegisterBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.register.viewModel.StudentRegisterViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudentRegisterFragment : BaseFragment<FragmentStudentRegisterBinding>() {
    override val viewModel: StudentRegisterViewModelImpl by viewModels()
    private lateinit var classesAdapter: ArrayAdapter<String>
    private var classSelect = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        classesAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            emptyList()
        )

        binding.run {
            btnStudentRegister.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val pass = etPassword.text.toString()
                val confirmPass = etConfirmPassword.text.toString()
                viewModel.studentRegister(name, email, pass, confirmPass, classSelect)
            }

            autoCompleteCategory.addTextChangedListener {
                classSelect = it.toString()
                logMsg(classSelect)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.classesName.collect {
                if(it.isEmpty()){
                    classesAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        emptyList()
                    )
                } else{
                    classesAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        it
                    )
                }
                binding.autoCompleteCategory.setAdapter(classesAdapter)
            }
        }

        lifecycleScope.launch {
            viewModel.success.collect{
                val action = StudentRegisterFragmentDirections.actionGlobalLogin()
                navController.navigate(action)
            }
        }
    }
}