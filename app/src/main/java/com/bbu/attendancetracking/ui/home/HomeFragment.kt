package com.bbu.attendancetracking.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.data.LocalStorageHelper
import com.bbu.attendancetracking.data.model.LoginResponse
import com.bbu.attendancetracking.data.model.RecyclerViewItem
import com.bbu.attendancetracking.databinding.FragmentHomeBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewModel.fetchDataTrigger.observe(viewLifecycleOwner, Observer { shouldFetch ->
//            if (shouldFetch) {
                lifecycleScope.async {
                    try {

                        Log.d("Trigger", "Trigger Fetch Classes")
                        // Trigger the API fetch in your ViewModel
                        lifecycleScope.async {
                            viewModel.fetchClasses()
                        }.await()
                    } catch (e: Exception) {
                        // Handle the error
//                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
//            }
        })

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

       var loginResponse: LoginResponse? = LocalStorageHelper.getLoginResponse(MyApplication.instance.applicationContext)

        binding.customAppBar.profileName.text = loginResponse?.user?.lastName


        //hide Search In Case "Teacher" Profile

        binding.searchSection.visibility =
           if (loginResponse?.user?.roles?.any { it.equals("TEACHER", ignoreCase = true) } == true)
               View.GONE else View.VISIBLE



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView with adapter
        val adapter = MultiTypeAdapter(emptyList()){ index ->
            Log.d("Data: ", "index click: ${viewModel.classItems.value[index]}")


            when (val item = viewModel.classItems.value[index]) {
                is RecyclerViewItem.ClassItem -> {
                    println("ClassItem - Title: ${item.classId}")

                    var loginDetails: LoginResponse? = LocalStorageHelper.getLoginResponse(MyApplication.instance.applicationContext)

                    if((loginDetails?.user?.roles?.any { it.equals("TEACHER", ignoreCase = true) } == true)) {
                        val intent = Intent(requireActivity(), GenerateQrActivity::class.java)
                        intent.putExtra("classId", item.classId)
                        startActivity(intent)
                    }else {
                        findNavController().navigate(R.id.navigation_scan_qr)
                    }


                }
                else -> {
                    println("Unknown item type")
                }
            }

        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        lifecycleScope.async {
            binding.loading.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE

            // Await the fetchClasses coroutine
            lifecycleScope.async {
                viewModel.fetchClasses()
            }.await()

            if(viewModel.classItems.value.isEmpty()) {
                binding.emptyText.text = "No Record Found!"
                binding.emptyText.visibility = View.VISIBLE
            }else {
                binding.emptyText.visibility = View.GONE
            }

            // Only execute this after fetchClasses finishes
            binding.loading.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }

        Log.d("AAAAAA", "On View Create")

        // Collect StateFlow data
        observeViewModel(adapter)

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition >= totalItemCount) && firstVisibleItemPosition >= 0) {
                    lifecycleScope.launch {

                        viewModel.fetchClasses()

//                        binding.loading.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun observeViewModel(adapter: MultiTypeAdapter) {
        // Collect class items from ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.classItems.collectLatest { items ->
                adapter.updateItems(items) // Update the adapter's data dynamically
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
