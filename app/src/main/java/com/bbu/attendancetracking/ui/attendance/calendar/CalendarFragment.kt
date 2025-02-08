package com.bbu.attendancetracking.ui.attendance.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()



        val today = CalendarDay.today()

        lifecycleScope.launch {
            binding.loading.visibility = View.VISIBLE
            lifecycleScope.async {
                viewModel.getAttendanceHistory(
                    viewModel.classId,
                    today.month + 1,
                    today.year
                )
            }.await()

            binding.loading.visibility = View.GONE
        }
    }


    override fun onPause() {

        viewModel.triggerClear()

        super.onPause()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        // Retrieve data from arguments
        val selectedDate = arguments?.getString("selectedDate")
        val classTitle = arguments?.getString("classTitle")
//        val classId = arguments?.getInt("classId")

//        if(classId!=null){
//            viewModel.classId = classId
//        }

        Log.d("data::", selectedDate?:"")
        Log.d("data::", classTitle?:"")

        val calendarView = binding.materialCalendarView

        val today = CalendarDay.today()
        Log.d("data::", "${today.month}")


        // Set the current date
        calendarView.selectedDate = today



        // Handle date selection
        calendarView.setOnDateChangedListener(OnDateSelectedListener { _, date, _ ->

            Log.d("OUT", "GGGGGGGGG")

            calendarView.selectedDate = CalendarDay.today()
//            val selectedDate = "${date.day}/${date.month + 1}/${date.year}"
//            Toast.makeText(context, "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
        })

        calendarView.setOnMonthChangedListener { _, date ->
            val month = date.month
            val year = date.year


            if(today.month < month && year >= today.year) { //month > this month will be not call api
                return@setOnMonthChangedListener
            }

            binding.loading.visibility = View.VISIBLE

            lifecycleScope.launch {
                lifecycleScope.async {
                    viewModel.getAttendanceHistory(
                        viewModel.classId,
                        month + 1,
                        year
                    )
                }.await()
                binding.loading.visibility = View.GONE
            }
            Log.d("Month Changed", "New Month: $month, New Year: $year")
        }

        // Set an OnMonthChangedListener


//        calendarView.setOnMonthChangedListener()


        viewModel.lateDatesList.observe(viewLifecycleOwner){

            val collection: Collection<CalendarDay> = viewModel.lateDatesList.value ?: emptyList()
            binding.late.text = "${viewModel.lateDatesList.value?.size} D"

            calendarView.addDecorators(
                EventDecorator(collection , ContextCompat.getColor(requireContext(), R.color.yellow)),
            )

        }
        viewModel.presentDatesList.observe(viewLifecycleOwner){

            val collection: Collection<CalendarDay> = viewModel.presentDatesList.value ?: emptyList()

            binding.present.text = "${viewModel.presentDatesList.value?.size} D"

            calendarView.addDecorators(
                EventDecorator(collection, ContextCompat.getColor(requireContext(), R.color.green))
            )

        }
        viewModel.absentDatesList.observe(viewLifecycleOwner){

            val collection: Collection<CalendarDay> = viewModel.absentDatesList.value ?: emptyList()

            binding.absent.text = "${viewModel.absentDatesList.value?.size} D"

            calendarView.addDecorators(
                EventDecorator(collection, ContextCompat.getColor(requireContext(), R.color.red))
            )
        }

        viewModel.triggerClear.observe(viewLifecycleOwner){
            calendarView.removeDecorators()
            calendarView.invalidate()
        }

        // Highlight dates
//        highlightDates(calendarView)

        return binding.root
    }



    private fun cleardata(calendarView: MaterialCalendarView) {


        val presentDates = listOf(
            CalendarDay.from(2000, 0, 1),  // Sample Date
            CalendarDay.from(2000, 0, 2)
        )

        val lateDates = listOf(
            CalendarDay.from(2000, 1, 3)
        )

        val absentDates = listOf(
            CalendarDay.from(2000, 1, 4),
            CalendarDay.from(2000, 11, 9)
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
