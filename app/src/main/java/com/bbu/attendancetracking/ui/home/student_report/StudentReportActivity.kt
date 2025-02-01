package com.bbu.attendancetracking.ui.home.student_report


import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.databinding.ActivityStudentReportBinding

class StudentReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentReportBinding
    private val viewModel: StudentReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.colorPrimary) // Set your desired color here

        val adapter = StudentAdapter(viewModel.students.value ?: listOf()) { position, status ->
            viewModel.updateStatus(position, status)
        }

        //data

//        val classId = intent.getIntExtra("classId", -1)
        val title = intent.getStringExtra("classTitle");
        val scheduleClass = intent.getStringExtra("classSchedule");

        binding.tvTitle.text = title
        binding.tvDate.text = scheduleClass
//        binding.

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.appbar.backButton.setOnClickListener{
            finish()
        }

        viewModel.students.observe(this) { students ->
            adapter.notifyDataSetChanged()
        }
    }
}
