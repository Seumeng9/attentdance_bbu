package com.bbu.attendancetracking.ui.attendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bbu.attendancetracking.ui.attendance.calendar.CalendarFragment
import com.bbu.attendancetracking.ui.attendance.report.ClassReportFragment

class AttendancePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2 // We have 2 tabs

    var calendarArgs: Bundle? = null // Store arguments for CalendarFragment


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClassReportFragment()    // Report tab
            1 -> CalendarFragment() // Calendar tab
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
