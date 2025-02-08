package com.bbu.attendancetracking.ui.home.student_report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.model.ClassItem

class StudentAdapter(
    private var students: List<Student>,
    private val onStatusChange: (Int, String) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val email: TextView = itemView.findViewById(R.id.tvEmail)
        val radioGroup: RadioGroup = itemView.findViewById(R.id.rgAttendance)

        fun bind(student: Student, position: Int) {
            name.text = student.name
            email.text = student.email
            radioGroup.setOnCheckedChangeListener(null)

            when (student.status) {
                AttendanceStatus.present.name -> radioGroup.check(R.id.rbPresent)
                AttendanceStatus.late.name -> radioGroup.check(R.id.rbLate)
                AttendanceStatus.absent.name -> radioGroup.check(R.id.rbAbsent)
            }

            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                val status = when (checkedId) {
                    R.id.rbPresent -> AttendanceStatus.present.name
                    R.id.rbLate -> AttendanceStatus.late.name
                    R.id.rbAbsent -> AttendanceStatus.absent.name
                    else -> AttendanceStatus.absent.name
                }
                onStatusChange(position, status)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student_report, parent, false)
        return StudentViewHolder(view)
    }

    fun updateData(newList: List<Student>) {
        this.students = newList
        notifyDataSetChanged()  // Notify RecyclerView to refresh
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(students[position], position)
    }

    override fun getItemCount() = students.size
}
