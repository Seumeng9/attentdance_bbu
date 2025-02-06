package com.bbu.attendancetracking.ui.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.helpers.LocalStorageHelper
import com.bbu.attendancetracking.databinding.FragmentHomeBinding

import com.bbu.attendancetracking.model.LoginResponse
import com.bbu.attendancetracking.model.RecyclerViewItem
import kotlinx.coroutines.Job

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    private var debounceJob: Job? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

//        viewModel.fetchDataTrigger.observe(viewLifecycleOwner, Observer { shouldFetch ->
////            if (shouldFetch) {
//                lifecycleScope.async {
//                    try {
//
//                        Log.d("Trigger", "Trigger Fetch Classes")
//                        // Trigger the API fetch in your ViewModel
//                        lifecycleScope.async {
//                            viewModel.fetchClasses()
//                        }.await()
//                    } catch (e: Exception) {
//                        // Handle the error
////                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
//                    }
//                }
////            }
//        })

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

       var loginResponse: LoginResponse? = LocalStorageHelper.getLoginResponse()

        binding.customAppBar.profileName.text = loginResponse?.user?.lastName


        //hide Search In Case "Teacher" Profile

//        binding.searchSection.visibility =
//           if (loginResponse?.user?.roles?.any { it.equals("TEACHER", ignoreCase = true) } == true)
//               View.GONE else View.VISIBLE



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

                    var loginDetails: LoginResponse? = LocalStorageHelper.getLoginResponse()

                    if((loginDetails?.user?.roles?.any { it.equals("TEACHER", ignoreCase = true) } == true)) {
                        val intent = Intent(requireActivity(), GenerateQrActivity::class.java)
                        intent.putExtra("classId", item.classId)
                        intent.putExtra("classTitle", item.title)
                        intent.putExtra("classSchedule", item.scheduled)
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


        // Access your EditText from the fragment layout
        val searchText: EditText = binding.searchTextField

        // Add TextWatcher to the EditText
        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Cancel any ongoing job
                debounceJob?.cancel()
            }

            override fun afterTextChanged(s: Editable?) {
                debounceJob?.cancel() // Cancel previous job if a new change occurs
                debounceJob = lifecycleScope.launch {
                    delay(2000) // Wait for 2 seconds of inactivity

                    val inputText = s.toString()

                    println("Debounced Input: $inputText")

                    if(viewModel.tempClassItem.isEmpty() && inputText.isEmpty()) {
                        return@launch
                    }



                    if(inputText.isNotEmpty()) {
                        binding.loading.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE

                        if(viewModel.tempClassItem.isEmpty() && viewModel.classItems.value.isNotEmpty()) {
                            viewModel.tempClassItem = viewModel.classItems.value
                        }

                        try {
                            viewModel.currentPage = 1
                            viewModel.fetchClasses(page = 1, filter = inputText)

                            if(viewModel.classItems.value.isEmpty()) {
                                binding.emptyText.visibility = View.VISIBLE
                            }else {
                                binding.emptyText.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                            println("Error fetching classes: ${e.message}")
                        }
                    }else {
                        viewModel.updateClass(viewModel.tempClassItem)
                        viewModel.tempClassItem = emptyList()
                        return@launch
                    }



                    println("after Debounced Input: $inputText")

                    binding.loading.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
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
