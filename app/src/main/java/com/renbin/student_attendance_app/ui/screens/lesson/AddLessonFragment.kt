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
    // Initialize the ViewModel using viewModels delegate
    override val viewModel: AddLessonViewModelImpl by viewModels()

    // Adapter for the class selection dropdown
    private lateinit var classesAdapter: ArrayAdapter<String>

    // Selected class, date, and time for the lesson
    private var classSelect = ""
    private var date = ""
    private var time = ""

    // Override onCreateView to inflate the fragment's layout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Override setupUIComponents to initialize UI elements and set up event listeners
    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        // Initialize ArrayAdapter for class selection dropdown
        classesAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            emptyList()
        )

        binding.run {
            // Navigate back when the back button is clicked
            ibBack.setOnClickListener {
                navController.popBackStack()
            }

            // Show date picker when the date button is clicked
            btnDate.setOnClickListener {
                datePicker()
            }

            // Show time picker when the time button is clicked
            btnTime.setOnClickListener {
                timePicker()
            }

            // Update selected class based on user input
            autoCompleteClass.addTextChangedListener {
                classSelect = it.toString()
            }

            // Trigger the lesson addition when the submit button is clicked
            btnSubmit.setOnClickListener {
                val name = etLessonName.text.toString()
                val details = etLessonDetails.text.toString()

                // Call the ViewModel function to add the lesson
                viewModel.addLesson(name, details, classSelect, date, time, requireContext())
            }
        }
    }

    // Override setupViewModelObserver to observe changes in the ViewModel and update the UI accordingly
    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            // Observe changes in the list of class names
            viewModel.classesName.collect {
                classesAdapter = if (it.isEmpty()) {
                    ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        emptyList()
                    )
                } else {
                    ArrayAdapter(
                        requireContext(),
                        androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                        it
                    )
                }
                // Set the adapter for the class selection dropdown
                binding.autoCompleteClass.setAdapter(classesAdapter)
            }
        }

        lifecycleScope.launch {
            // Observe success events from the ViewModel
            viewModel.success.collect {
                // Navigate back upon successful lesson addition
                navController.popBackStack()
            }
        }
    }

    // Show the date picker dialog and update the selected date
    private fun datePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker().setTitleText("Select date of lesson").build()
        datePicker.show(this.parentFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener {
            date = formatDatestamp(it)
            // Update the date button text
            binding.btnDate.text = date
        }
    }

    // Show the time picker dialog and update the selected time
    private fun timePicker() {
        // Create a MaterialTimePicker dialog for selecting the time
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText("Select time of lesson")
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .build()

        // Show the time picker dialog using the parent FragmentManager
        timePicker.show(this.parentFragmentManager, "TIME_PICKER")

        // Listen for a positive button click to update the selected time
        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute

            // Format the selected time based on the 12-hour clock format
            time = when {
                hour > 12 -> String.format("%02d:%02d pm", hour - 12, minute)
                hour == 12 -> String.format("%02d:%02d pm", hour, minute)
                hour == 0 -> String.format("%02d:%02d am", hour + 12, minute)
                else -> String.format("%02d:%02d am", hour, minute)
            }

            // Update the text of the time button with the selected time
            binding.btnTime.text = time
        }
    }
}