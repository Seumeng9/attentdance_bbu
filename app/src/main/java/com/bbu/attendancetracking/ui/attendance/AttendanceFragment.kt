package com.bbu.attendancetracking.ui.attendance

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bbu.attendancetracking.databinding.FragmentAttendanceBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.ui.attendance.calendar.CalendarViewModel

class AttendanceFragment : Fragment(R.layout.fragment_attendance) {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!


    private val calendarViewModel: CalendarViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)


        // Set up ViewPager2 and TabLayout
         val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabLayout


        val pagerAdapter = AttendancePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        // Link TabLayout and ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Report"  else "Calendar"
        }.attach()


        //interceptor switch tab

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
//                    val newPosition = it.position
                    
                    try {
                        if(calendarViewModel.classId == 0 ){

                            Handler(Looper.getMainLooper()).post {
                                Log.d("on tap", "click caledar ta1111b")
                                Toast.makeText(MyApplication.instance.applicationContext, "Please Select Class To See Report!!!", Toast.LENGTH_SHORT).show()
                                tabLayout.getTabAt(0)?.select()
                            }
                        }
                    } catch (e: Exception){
                        if(it.position != 0){
                            tabLayout.getTabAt(0)?.select()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        //


        return binding.root
    }

    // Method to switch to a specific tab (called from child fragment)
//    fun switchToTab(position: Int) {
//        Handler(Looper.getMainLooper()).post {
//            tabLayout.getTabAt(0)?.select()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
