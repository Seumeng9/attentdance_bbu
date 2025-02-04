package com.bbu.attendancetracking.ui.scanqr

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import android.Manifest
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log


import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.ui.home.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ScanQrFragment : Fragment() {

    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private lateinit var resultText: TextView
    private lateinit var backButton: ImageButton
    private val viewModel: ScanQrViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient



    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 101
        const val LOCATION_PERMISSION_REQUEST_CODE = 102
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_scanqr, container, false)



        backButton = view.findViewById(R.id.back_button)
        backButton.setOnClickListener(){
            handleBackNavigation()
        }


        // Initialize views
        barcodeScannerView = view.findViewById(R.id.barcode_scanner)
        resultText = view.findViewById(R.id.resultText)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


        // Request camera permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            startScanning()
        }


        // Observe error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Observe success messages
        viewModel.successMessage.observe(viewLifecycleOwner) { successMessage ->
            Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show()
            // Navigate back to the previous screen on success
            findNavController().navigateUp()
        }

        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning()
            } else {
                Toast.makeText(requireContext(), "Camera permission is required for scanning", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleBackNavigation() {
        barcodeScannerView.pause() // Pause the scanner
        findNavController().navigateUp() // Navigate back to the previous destination
    }

    private fun startScanning() {
        barcodeScannerView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {



                    barcodeScannerView.pause()

                    // Fetch the current location
                    getCurrentLocation { lat, long ->
                        // Extract classId from the scanned QR code
                        val qrContent = it.text // Example: "CLASS_ID:202"
                        val regex = """CLASS_ID:(\S+)""".toRegex()
                        val matchResult = regex.find(qrContent)

                        try {
                            val classId = matchResult?.groupValues?.get(1)?.toInt()

                            // Show the loading indicator
                            val loadingView = requireView().findViewById<View>(R.id.loading)
                            loadingView.visibility = View.VISIBLE




                            // Call the ViewModel method in a coroutine
                            lifecycleScope.async {
                                try {
                                    lifecycleScope.async {
                                        viewModel.submitAttendant(classId ?: 0, lat.toString(), long.toString())
//                                        viewModel.submitAttendant(classId ?: 0, "11.516647729836077", "104.95535159581806")
                                    }.await() // Call API

                                    // After successful submission, navigate back or show success UI
                                    findNavController().navigateUp()

                                    // Trigger the fetch in HomeFragment once navigation back occurs
                                    val homeViewModel =
                                        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
                                    homeViewModel.triggerFetchData() // Notify HomeFragment to fetch data
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        requireContext(),
                                        "Error: ${viewModel.errorMessage}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } finally {
                                    // Hide the loading indicator
                                    loadingView.visibility = View.GONE

                                    // Stop scanning after handling the result

                                    barcodeScannerView.visibility = View.GONE
                                }
                            }
                        } catch (e: NumberFormatException) {
                            Toast.makeText(
                                requireContext(),
                                "Invalid QR code format!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            override fun possibleResultPoints(resultPoints: MutableList<com.google.zxing.ResultPoint>?) {
                // Not needed for basic functionality
            }
        })
        barcodeScannerView.resume()
    }

    private fun getCurrentLocation(callback: (Double, Double) -> Unit) {

        // Check if the permission is granted, if not load request permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Get the last known location
        fusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val location = task.result
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    callback(latitude, longitude) // Pass the coordinates to the callback
                } else {
                    Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onPause() {
        super.onPause()
        barcodeScannerView.pause() // Pause the scanner when the fragment is paused
    }

    override fun onResume() {
        super.onResume()

        barcodeScannerView.resume() // Resume the scanner when the fragment is resumed
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

}


