package com.bbu.attendancetracking.ui.attendance.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.databinding.FragmentClassReportBinding
import com.bbu.attendancetracking.ui.attendance.AttendanceFragment
import com.bbu.attendancetracking.ui.attendance.calendar.CalendarViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ClassReportFragment : Fragment(R.layout.fragment_class_report) {

    private var _binding: FragmentClassReportBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReportAttendanceViewModel by viewModels()

    private val calendarViewModel: CalendarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassReportBinding.inflate(inflater, container, false)

//        val adapter = viewModel.classList.value?.let { ClassReportAttendanceAdapter(it) }

//        val attendanceFragment = requireParentFragment() as AttendanceFragment

        val viewPager = requireParentFragment().requireView().findViewById<ViewPager2>(R.id.viewPager)


        val adapter = ClassReportAttendanceAdapter(
            viewModel.classList.value ?: emptyList(),
            calendarViewModel,
            viewPager)


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter


        lifecycleScope.launch {
            lifecycleScope.async {
                viewModel.fetchClasses()
            }.await()

            if(viewModel.classList.value?.isNotEmpty() == true) {
                binding.emptyText.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }else {
                binding.emptyText.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }


        viewModel.classList.observe(viewLifecycleOwner){
            adapter.updateData(it)

        }

        viewModel.isLoading.observe(viewLifecycleOwner){
            if(viewModel.isLoading.value == true) {
                binding.loading.visibility = View.VISIBLE
            }else {
                binding.loading.visibility = View.GONE
            }
        }





        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
