package com.renbin.student_attendance_app.ui.screens.tabContainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.renbin.student_attendance_app.databinding.FragmentStudentTabContainerBinding
import com.renbin.student_attendance_app.ui.adapter.FragmentAdapter
import com.renbin.student_attendance_app.ui.screens.classes.StudentClassesFragment
import com.renbin.student_attendance_app.ui.screens.home.StudentHomeFragment
import com.renbin.student_attendance_app.ui.screens.note.NoteFragment
import com.renbin.student_attendance_app.ui.screens.profile.StudentProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentTabContainerFragment : Fragment() {
    private lateinit var binding: FragmentStudentTabContainerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentTabContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpStudentContainer.adapter = FragmentAdapter(
            this,
            listOf(StudentHomeFragment(), StudentClassesFragment(), NoteFragment(), StudentProfileFragment())
        )

        TabLayoutMediator(binding.tlStudentTabs, binding.vpStudentContainer){ tab, position ->
            when(position){
                0 ->{
                    tab.text = "Home"
                }
                1 -> {
                    tab.text = "Class"
                }
                2 ->{
                    tab.text = "Note"
                }
                else ->{
                    tab.text = "Profile"
                }
            }
        }.attach()
    }
}