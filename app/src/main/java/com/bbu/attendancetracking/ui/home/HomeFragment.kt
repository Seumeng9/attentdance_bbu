package com.bbu.attendancetracking.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.data.model.ClassItem
import com.bbu.attendancetracking.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
//    private lateinit var bottomNavigationView: BottomNavigationView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up custom app bar
        val customAppBar = binding.root.findViewById<ConstraintLayout>(R.id.customAppBar)




        // Set up RecyclerView
        val classList = listOf(
            ClassItem("Mobile Programming 1", "Saturday, 20th April (8:00am - 13:00pm)", "LAB: 907"),
            ClassItem("Database System", "Tuesday, 23rd April (8:30am - 11:30am)", "LAB: 908"),
            ClassItem("Software Requirement", "Saturday, 20th April (8:00am - 13:00pm)", "LAB: 907")
        )


//        bottomNavigationView = requireActivity().findViewById(R.id.nav_view)


        val adapter = ClassAdapter(classList)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}