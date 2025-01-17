package com.bbu.attendancetracking.ui.dashboard

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
import com.bbu.attendancetracking.R
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class DashboardFragment : Fragment() {

    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private lateinit var resultText: TextView
    private lateinit var backButton: ImageButton


    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 101
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
        barcodeScannerView.decodeSingle(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    // Update the resultText with scanned QR code content
                    resultText.text = "Scanned Result: ${it.text}"

                    // Stop scanning after the first scan
                    barcodeScannerView.pause()

                    // Optionally, hide the camera or other UI elements here
                    barcodeScannerView.visibility = View.GONE
                }
            }

            override fun possibleResultPoints(resultPoints: MutableList<com.google.zxing.ResultPoint>?) {
                // Not needed for basic functionality
            }
        })
        barcodeScannerView.resume()
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


