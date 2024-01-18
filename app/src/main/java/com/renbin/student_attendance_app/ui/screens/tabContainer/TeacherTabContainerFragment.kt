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
import com.renbin.student_attendance_app.ui.screens.note.TeacherNoteFragment
import com.renbin.student_attendance_app.ui.screens.profile.TeacherProfileFragment
import com.renbin.student_attendance_app.ui.screens.teacherQuizHomePage.TeacherQuizHomeFragment
import dagger.hilt.android.AndroidEntryPoint

// Dagger Hilt AndroidEntryPoint annotation
@AndroidEntryPoint
class TeacherTabContainerFragment : Fragment() {
    // Initialize ViewBinding
    private lateinit var binding: FragmentTeacherTabContainerBinding

    // Function to create the view for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTeacherTabContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Function to set up the ViewPager, TabLayout, and custom tabs
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up ViewPager with FragmentAdapter
        binding.vpTeacherContainer.adapter = FragmentAdapter(
            this,
            listOf(
                TeacherClassesFragment(),
                TeacherLessonFragment(),
                TeacherHomeFragment(),
                TeacherNoteFragment(),
                TeacherQuizHomeFragment(),
                TeacherProfileFragment()
            )
        )

        // Set up TabLayout with custom tab icons
        TabLayoutMediator(binding.tlTeacherTabs, binding.vpTeacherContainer) { tab, position ->
            // Inflate custom tab view
            val customTabView = layoutInflater.inflate(R.layout.custom_tab_layout, null)
            val tabGifIcon = customTabView.findViewById<pl.droidsonroids.gif.GifImageView>(R.id.tabGifIcon)

            // Set custom tab icon based on the position
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
                4 -> {
                    tabGifIcon.setImageResource(R.drawable.quiz)
                }
                else -> {
                    tabGifIcon.setImageResource(R.drawable.profile)
                }
            }

            // Set custom view for the tab
            tab.customView = customTabView
        }.attach()

        // Set initial ViewPager item and disable user input
        binding.vpTeacherContainer.setCurrentItem(2, false)
        binding.vpTeacherContainer.isUserInputEnabled = false
    }
}