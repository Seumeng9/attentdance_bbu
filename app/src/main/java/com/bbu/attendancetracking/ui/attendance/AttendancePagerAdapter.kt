package com.bbu.attendancetracking.ui.attendance

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AttendancePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2 // We have 2 tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CalendarFragment()  // Calendar tab
            1 -> ReportFragment()    // Report tab
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
