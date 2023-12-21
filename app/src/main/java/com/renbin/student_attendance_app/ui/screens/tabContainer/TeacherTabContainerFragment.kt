package com.renbin.student_attendance_app.ui.screens.tabContainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentTeacherTabContainerBinding
import com.renbin.student_attendance_app.ui.adapter.FragmentAdapter
import com.renbin.student_attendance_app.ui.screens.classes.TeacherClassesFragment
import com.renbin.student_attendance_app.ui.screens.home.TeacherHomeFragment
import com.renbin.student_attendance_app.ui.screens.lesson.TeacherLessonFragment
import com.renbin.student_attendance_app.ui.screens.note.NoteFragment
import com.renbin.student_attendance_app.ui.screens.profile.TeacherProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherTabContainerFragment : Fragment() {
    private lateinit var binding: FragmentTeacherTabContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTeacherTabContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpTeacherContainer.adapter = FragmentAdapter(
            this,
            listOf(
                TeacherClassesFragment(),
                TeacherLessonFragment(),
                TeacherHomeFragment(),
                NoteFragment(),
                TeacherProfileFragment()
            )
        )

        TabLayoutMediator(binding.tlTeacherTabs, binding.vpTeacherContainer){ tab, position ->
            when(position){
                0 ->{
                    tab.setIcon(R.drawable.ic_class)
                }
                1 -> {
                    tab.setIcon(R.drawable.ic_notifications)
                }
                2->{
                    tab.setIcon(R.drawable.ic_home)
                }
                3 ->{
                    tab.setIcon(R.drawable.ic_book)
                }
                else ->{
                    tab.setIcon(R.drawable.ic_person)
                }
            }
        }.attach()

        binding.vpTeacherContainer.setCurrentItem(2, false)
        binding.vpTeacherContainer.isUserInputEnabled = false
    }

}