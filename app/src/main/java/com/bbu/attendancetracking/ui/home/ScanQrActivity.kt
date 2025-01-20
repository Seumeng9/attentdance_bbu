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

class ScanQrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenerateQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityGenerateQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle the button click for generating the QR code
        binding.generateQrButton.setOnClickListener {
            val qrContent = "https://www.example.com" // Replace with the content you want in the QR code
            generateQrCode(qrContent)
        }
    }

    private fun generateQrCode(content: String) {
        try {
            // Initialize the QR code writer
            val qrCodeWriter = QRCodeWriter()

            // Set encoding hints (optional)
            val hints = hashMapOf<EncodeHintType, Any>(
                EncodeHintType.MARGIN to 1  // Sets the margin size around the QR code
            )

            // Create the BitMatrix (binary representation of the QR code)
            val bitMatrix: BitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 500, 500, hints)

            // Convert BitMatrix to Bitmap
            val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565)
            for (x in 0 until 500) {
                for (y in 0 until 500) {
                    bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }

            // Set the generated QR code as the source of the ImageView
            binding.qrImageView.setImageBitmap(bitmap)

        } catch (e: Exception) {
            // Handle any errors (e.g., invalid content)
            Toast.makeText(this, "Error generating QR code: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
