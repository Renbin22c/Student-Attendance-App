package com.renbin.student_attendance_app.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// Define FragmentAdapter class extending FragmentStateAdapter
class FragmentAdapter(
    fragment: Fragment,
    private val tabs: List<Fragment>
): FragmentStateAdapter(fragment) {

    // Return the number of tabs
    override fun getItemCount() = tabs.size

    // Return the fragment for the given position
    override fun createFragment(position: Int): Fragment {
        return tabs[position]
    }
}