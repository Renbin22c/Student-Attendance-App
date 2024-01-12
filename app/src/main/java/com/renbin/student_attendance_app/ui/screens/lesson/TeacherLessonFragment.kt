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
    override val viewModel: TeacherLessonViewModelImpl by viewModels()
    private lateinit var lessonAdapter: LessonAdapter
    private lateinit var classAdapter: ArrayAdapter<String>
    private lateinit var dateAdapter: ArrayAdapter<String>
    private var classSelect: String? = null

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

        classAdapter = ArrayAdapter(
            requireContext(),
            androidx.transition.R.layout.support_simple_spinner_dropdown_item,
            emptyList()
        )


        binding.run {
            btnAddLesson.setOnClickListener {
                clearOption()

                val action = TeacherTabContainerFragmentDirections.actionTeacherTabContainerToAddLesson()
                navController.navigate(action)
            }

            autoCompleteClass.addTextChangedListener {
                classSelect = it.toString()
                autoCompleteClass.clearFocus()
                viewModel.filterLessons(classSelect)
            }

            btnClear.setOnClickListener {
                classSelect = null
                clearOption()
                viewModel.getAllLesson()
                viewModel.filterLessons(null)
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.lessons.collect{
                if (classSelect == null){
                    lessonAdapter.setLessons(it)
                }
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
            viewModel.classes.collect{
                classAdapter = ArrayAdapter(
                    requireContext(),
                    androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                    it.sorted()
                )
                binding.autoCompleteClass.setAdapter(classAdapter)
            }
        }


        lifecycleScope.launch {
            viewModel.filteredLessons.collect {
                if (classSelect !=null){
                    lessonAdapter.setLessons(it)
                }
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
        lessonAdapter = LessonAdapter(emptyList(), emptyList(), emptyList(), viewModel.user)
        lessonAdapter.listener = object : LessonAdapter.Listener{
            override fun onClick(id: String, lesson: Lesson) {
                TODO("Not yet implemented")
            }

            override fun onDelete(lesson: Lesson) {
                viewModel.deleteLesson(lesson.id.toString())
            }

        }
        binding.rvLesson.adapter = lessonAdapter
        binding.rvLesson.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun clearOption(){
        binding.run {
            classSelect = null

            autoCompleteClass.text.clear()
            autoCompleteClass.clearFocus()
        }
    }


}