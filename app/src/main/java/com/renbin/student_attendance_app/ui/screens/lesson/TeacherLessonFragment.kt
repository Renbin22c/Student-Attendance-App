package com.renbin.student_attendance_app.ui.screens.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.databinding.FragmentTeacherLessonBinding
import com.renbin.student_attendance_app.ui.adapter.LessonAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.lesson.viewModel.TeacherLessonViewModelImpl
import com.renbin.student_attendance_app.ui.screens.tabContainer.TeacherTabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TeacherLessonFragment : BaseFragment<FragmentTeacherLessonBinding>() {
    // ViewModel instance for the TeacherLessonFragment
    override val viewModel: TeacherLessonViewModelImpl by viewModels()

    // Adapter for displaying lessons in the RecyclerView
    private lateinit var lessonAdapter: LessonAdapter

    // Adapter for the class autocomplete dropdown
    private lateinit var classAdapter: ArrayAdapter<String>

    // Adapter for the time autocomplete dropdown
    private lateinit var timeAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTeacherLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupLessonAdapter()

        // ArrayAdapter for the class autocomplete dropdown
        classAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            emptyList()
        )

        // ArrayAdapter for the time autocomplete dropdown
        timeAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            emptyList()
        )

        binding.run {
            // Add Lesson button click listener to navigate to AddLessonFragment
            btnAddLesson.setOnClickListener {
                clearOption()

                val action = TeacherTabContainerFragmentDirections.actionTeacherTabContainerToAddLesson()
                navController.navigate(action)
            }

            // Enable user to change the text themself
            autoCompleteClass.keyListener = null
            autoCompleteTime.keyListener = null

            // Class autocomplete text change listener to update the selected class
            autoCompleteClass.addTextChangedListener {
                val text = it.toString().trim()
                viewModel.updateClassSelect(text.ifEmpty { null })
                autoCompleteClass.clearFocus()
            }

            // Class autocomplete text change listener to update the selected time
            autoCompleteTime.addTextChangedListener {
                val text = it.toString().trim()
                viewModel.updateTimeSelect(text.ifEmpty { null })
                autoCompleteTime.clearFocus()
            }

            // Clear button click listener to clear the selected class
            btnClear.setOnClickListener {
                clearOption()
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        // Observe filtered lessons and update the adapter
        lifecycleScope.launch {
            viewModel.filterLessons.collect {
                lessonAdapter.setLessons(it)
            }
        }

        // Observe students and update the adapter
        lifecycleScope.launch {
            viewModel.students.collect {
                lessonAdapter.setStudents(it)
            }
        }

        // Observe teachers and update the adapter
        lifecycleScope.launch {
            viewModel.teachers.collect {
                lessonAdapter.setTeachers(it)
            }
        }

        // Observe distinct classes and update the class dropdown adapter
        lifecycleScope.launch {
            viewModel.classes.collect {
                classAdapter = ArrayAdapter(
                    requireContext(),
                    androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                    it.sorted()
                )
                binding.autoCompleteClass.setAdapter(classAdapter)
            }
        }

        // Observe distinct times and update the time dropdown adapter
        lifecycleScope.launch {
            viewModel.time.collect{
                timeAdapter = ArrayAdapter(
                    requireContext(),
                    androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                    it
                )
                binding.autoCompleteTime.setAdapter(timeAdapter)
            }
        }

        // Observe loading state and update UI accordingly
        lifecycleScope.launch {
            viewModel.loading.collect {
                if (it) {
                    binding.progressbar.visibility = View.VISIBLE
                } else {
                    binding.progressbar.visibility = View.GONE
                    if (lessonAdapter.itemCount == 0) {
                        binding.tvEmpty.visibility = View.VISIBLE
                    } else {
                        binding.tvEmpty.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupLessonAdapter() {
        // Initialize and set up the LessonAdapter for the RecyclerView
        lessonAdapter = LessonAdapter(emptyList(), emptyList(), emptyList(), viewModel.user, "Lesson")
        lessonAdapter.listener = object : LessonAdapter.Listener {
            override fun onClick(id: String, lesson: Lesson) {
                // Handle item click action (not yet implemented)
                TODO("Not yet implemented")
            }

            override fun onDelete(lesson: Lesson) {
                // Handle delete action by calling the ViewModel's deleteLesson method
                viewModel.deleteLesson(lesson.id.toString())
            }
        }
        binding.rvLesson.adapter = lessonAdapter
        binding.rvLesson.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun clearOption() {
        // Clear the selected class and reset UI components
        binding.run {
            viewModel.updateClassSelect(null)
            viewModel.updateTimeSelect(null)

            autoCompleteTime.text.clear()
            autoCompleteTime.clearFocus()

            autoCompleteClass.text.clear()
            autoCompleteClass.clearFocus()
        }
    }
}
