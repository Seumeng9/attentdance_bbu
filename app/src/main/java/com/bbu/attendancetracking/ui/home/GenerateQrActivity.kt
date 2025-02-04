package com.bbu.attendancetracking.ui.home

import android.content.Intent
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
import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.helpers.LocalStorageHelper
import com.bbu.attendancetracking.ui.home.student_report.StudentReportActivity

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
        val title = intent.getStringExtra("classTitle");
        val scheduleClass = intent.getStringExtra("classSchedule");


        // Inflate the layout using ViewBinding
        binding = ActivityGenerateQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customAppBar.title.text = "Qr Generator"

        binding.classTitle.text = title

        binding.classDate.text = scheduleClass


        binding.customAppBar.backButton.setOnClickListener{
            finish()
        }

        binding.classTitle.setOnClickListener{
            val intent = Intent(this, StudentReportActivity::class.java)

            intent.putExtra("classId", classId)
            intent.putExtra("classTitle", title)
            intent.putExtra("classSchedule", scheduleClass)


            startActivity(intent)
        }

        if(LocalStorageHelper.getGeneratedQrClassId() == classId) {
            generateQrCode(classId)
        }



        // Handle the button click for generating the QR code
        binding.generateQrButton.setOnClickListener {
            generateQrCode(classId)
            LocalStorageHelper.saveGeneratedQrClassId(classId)
        }
    }

    private fun generateQrCode(classId: Int) {

        val content = "My QR Code For Scan Submit Attendance  CLASS_ID:$classId , Use This For Submit to API" // Replace with the content you want in the QR code

        try {
            val qrCodeWriter = QRCodeWriter()
            val size = 800 // Increase resolution for better scannability
            val hints = hashMapOf<EncodeHintType, Any>(
                EncodeHintType.CHARACTER_SET to "UTF-8", // Encoding for content
                EncodeHintType.MARGIN to 4 // Margin for readability
            )
            val bitMatrix: BitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hints)

            // Define colors
            val foregroundColor = Color.parseColor("#1E88E5") // Dark Blue
            val backgroundColor = Color.parseColor("#FFFFFF") // White

            // Create Bitmap with custom colors
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
            for (x in 0 until size) {
                for (y in 0 until size) {
                    val color = if (bitMatrix[x, y]) foregroundColor else backgroundColor
                    bitmap.setPixel(x, y, color)
                }
            }

            // Set Bitmap to ImageView
            binding.qrImageView.setImageBitmap(bitmap)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(MyApplication.instance.applicationContext, "Error generating QR code: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


}
