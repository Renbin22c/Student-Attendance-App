package com.renbin.student_attendance_app.ui.screens.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.renbin.student_attendance_app.data.model.Lesson
import com.renbin.student_attendance_app.databinding.FragmentStudentLessonBinding
import com.renbin.student_attendance_app.ui.adapter.LessonAdapter
import com.renbin.student_attendance_app.ui.screens.base.BaseFragment
import com.renbin.student_attendance_app.ui.screens.lesson.viewModel.StudentLessonViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StudentLessonFragment : BaseFragment<FragmentStudentLessonBinding>() {
    override val viewModel: StudentLessonViewModelImpl by viewModels()
    private lateinit var lessonAdapter: LessonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)
        setupLessonAdapter()
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

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
        lessonAdapter = LessonAdapter(emptyList(), emptyList(), emptyList(), viewModel.user, "Lesson")
        lessonAdapter.listener = object : LessonAdapter.Listener{
            override fun onClick(id: String, lesson: Lesson) {
                viewModel.updateAttendanceStatus(id, lesson)
            }

            override fun onDelete(lesson: Lesson) {
                TODO("Not yet implemented")
            }

        }
        binding.rvLesson.adapter = lessonAdapter
        binding.rvLesson.layoutManager = LinearLayoutManager(requireContext())
    }
}