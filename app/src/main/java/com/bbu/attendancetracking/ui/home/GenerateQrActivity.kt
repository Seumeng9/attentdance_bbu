package com.bbu.attendancetracking.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bbu.attendancetracking.databinding.ActivityGenerateQrBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.common.BitMatrix
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.navigation.findNavController
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.data.model.RecyclerViewItem

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

class GenerateQrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenerateQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.colorPrimary) // Set your desired color here
        }

        val classId = intent.getIntExtra("classId", -1)




        // Inflate the layout using ViewBinding
        binding = ActivityGenerateQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customAppBar.title.text = "Qr Generator"


        binding.customAppBar.backButton.setOnClickListener{
            finish()
        }

        // Handle the button click for generating the QR code
        binding.generateQrButton.setOnClickListener {
            val qrContent = "CLASS_ID:$classId" // Replace with the content you want in the QR code
            generateQrCode(qrContent)
        }
    }

    private fun generateQrCode(content: String) {
        try {
            // Initialize the QR code writer
            val qrCodeWriter = QRCodeWriter()

            // Set encoding hints (optional)
            val hints = hashMapOf<EncodeHintType, Any>(
                EncodeHintType.MARGIN to 2 // Increase margin for better readability
            )

            // Create the BitMatrix (binary representation of the QR code)
            val bitMatrix: BitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 800, 800, hints)

            // Convert BitMatrix to Bitmap
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }

            // Set the generated QR code as the source of the ImageView
            binding.qrImageView.setImageBitmap(bitmap)

        } catch (e: Exception) {
            // Handle any errors (e.g., invalid content)
//            Toast.makeText(requireContext(), "Error generating QR code: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
