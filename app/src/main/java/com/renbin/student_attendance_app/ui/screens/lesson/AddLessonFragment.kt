package com.renbin.student_attendance_app.ui.screens.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.renbin.student_attendance_app.core.util.Utility.formatDatestamp
import com.renbin.student_attendance_app.databinding.FragmentAddLessonBinding
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.lesson.viewModel.AddLessonViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddLessonFragment: BaseFragment<FragmentAddLessonBinding>() {
    override val viewModel: AddLessonViewModelImpl by viewModels()
    private lateinit var classesAdapter: ArrayAdapter<String>
    private var classSelect = ""
    private var date = ""
    private var time = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddLessonBinding.inflate(inflater, container, false)
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
            ibBack.setOnClickListener {
                navController.popBackStack()
            }

            btnDate.setOnClickListener {
                datePicker()
            }

            btnTime.setOnClickListener {
                timePicker()
            }

            autoCompleteClass.addTextChangedListener {
                classSelect = it.toString()
            }

            btnSubmit.setOnClickListener {
                val name = binding.etLessonName.text.toString()
                val details = binding.etLessonDetails.text.toString()

                viewModel.addLesson(name, details, classSelect, date, time, requireContext())
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.classesName.collect {
                classesAdapter = if(it.isEmpty()){
                    ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        emptyList()
                    )
                } else{
                    ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        it
                    )
                }
                binding.autoCompleteClass.setAdapter(classesAdapter)
            }
        }

        lifecycleScope.launch{
            viewModel.success.collect{
                navController.popBackStack()
            }
        }
    }

    private fun datePicker(){
        val datePicker =
            MaterialDatePicker.Builder.datePicker().setTitleText("Select date of lesson").build()
        datePicker.show(this.parentFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener {
            date = formatDatestamp(it)
            binding.btnDate.text = date
        }
    }

    private fun timePicker(){
        val timePicker =
            MaterialTimePicker.Builder().setTitleText("Select time of lesson").setTimeFormat(
                TimeFormat.CLOCK_12H).build()
        timePicker.show(this.parentFragmentManager, "TIME_PICKER")

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            time = when {
                hour > 12 -> String.format("%02d:%02d pm", hour - 12, minute)
                hour == 12 -> String.format("%02d:%02d pm", hour, minute)
                hour == 0 -> String.format("%02d:%02d am", hour + 12, minute)
                else -> String.format("%02d:%02d am", hour, minute)
            }
            binding.btnTime.text = time
        }
    }
}