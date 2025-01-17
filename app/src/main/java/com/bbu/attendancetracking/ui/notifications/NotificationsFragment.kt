package com.bbu.attendancetracking.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.databinding.FragmentAttendanceBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener

class NotificationsFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        val calendarView = binding.materialCalendarView

        // Set the current date
        calendarView.selectedDate = CalendarDay.today()

        // Handle date selection
        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            val selectedDate = "${date.day}/${date.month + 1}/${date.year}"
            Toast.makeText(context, "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
        })

        // Highlight some dates (Present, Late, Absent)
        highlightDates(calendarView)



        return root
    }

    private fun highlightDates(calendarView: MaterialCalendarView) {
        val presentDates = listOf(
            CalendarDay.from(2025, 1, 1),  // April 1
            CalendarDay.from(2025, 1, 2)  // April 2
        )
        val lateDates = listOf(
            CalendarDay.from(2025, 2, 3)  // April 3
        )
        val absentDates = listOf(
            CalendarDay.from(2025, 1, 4),  // April 4
            CalendarDay.from(2025, 2, 5)  // April 5
        )

        calendarView.addDecorators(
            EventDecorator(presentDates, R.color.green),
            EventDecorator(lateDates, R.color.yellow),
            EventDecorator(absentDates, R.color.red)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}