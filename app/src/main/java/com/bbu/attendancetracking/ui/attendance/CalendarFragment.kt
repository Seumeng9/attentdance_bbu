package com.bbu.attendancetracking.ui.attendance

import EventDecorator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        val calendarView = binding.materialCalendarView

        // Set the current date
        calendarView.selectedDate = CalendarDay.today()

        // Handle date selection
        calendarView.setOnDateChangedListener(OnDateSelectedListener { _, date, _ ->
            val selectedDate = "${date.day}/${date.month + 1}/${date.year}"
            Toast.makeText(context, "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
        })

//        calendarView.setOnMonthChangedListener()

        // Highlight dates
        highlightDates(calendarView)

        return binding.root
    }

    private fun highlightDates(calendarView: MaterialCalendarView) {
        val presentDates = listOf(
            CalendarDay.from(2025, 0, 1),  // Sample Date
            CalendarDay.from(2025, 0, 2)
        )
        val lateDates = listOf(
            CalendarDay.from(2025, 1, 3)
        )
        val absentDates = listOf(
            CalendarDay.from(2025, 1, 4),
            CalendarDay.from(2025, 11, 9)
        )


        calendarView.addDecorators(
            EventDecorator(presentDates, ContextCompat.getColor(requireContext(), R.color.green)),
            EventDecorator(lateDates, ContextCompat.getColor(requireContext(), R.color.yellow)),
            EventDecorator(absentDates, ContextCompat.getColor(requireContext(), R.color.red))
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
