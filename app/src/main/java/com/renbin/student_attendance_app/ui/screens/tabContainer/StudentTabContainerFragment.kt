package com.renbin.student_attendance_app.ui.screens.tabContainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.renbin.student_attendance_app.R
import com.renbin.student_attendance_app.databinding.FragmentStudentTabContainerBinding
import com.renbin.student_attendance_app.ui.adapter.FragmentAdapter
import com.renbin.student_attendance_app.ui.screens.classes.StudentClassesFragment
import com.renbin.student_attendance_app.ui.screens.home.StudentHomeFragment
import com.renbin.student_attendance_app.ui.screens.lesson.StudentLessonFragment
import com.renbin.student_attendance_app.ui.screens.note.StudentNoteFragment
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
            listOf(
                StudentClassesFragment(),
                StudentLessonFragment(),
                StudentHomeFragment(),
                StudentNoteFragment(),
                StudentProfileFragment()
            )
        )

        TabLayoutMediator(binding.tlStudentTabs, binding.vpStudentContainer) { tab, position ->
            // Inflate custom tab view
            val customTabView = layoutInflater.inflate(R.layout.custom_tab_layout, null)
            val tabGifIcon = customTabView.findViewById<pl.droidsonroids.gif.GifImageView>(R.id.tabGifIcon)

            when (position) {
                0 -> {
                    tabGifIcon.setImageResource(R.drawable.presentation)

                }
                1 -> {
                    tabGifIcon.setImageResource(R.drawable.alarm)

                }
                2 -> {
                    tabGifIcon.setImageResource(R.drawable.home)
                }
                3 -> {
                    tabGifIcon.setImageResource(R.drawable.book)
                }
                else -> {
                    tabGifIcon.setImageResource(R.drawable.profile)
                }
            }

            // Set custom view for the tab
            tab.customView = customTabView
        }.attach()


        binding.vpStudentContainer.setCurrentItem(2, false)
        binding.vpStudentContainer.isUserInputEnabled = false
    }
}