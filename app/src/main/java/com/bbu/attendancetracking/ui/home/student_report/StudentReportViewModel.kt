package com.bbu.attendancetracking.ui.home.student_report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Student(val name: String, val email: String, var status: AttendanceStatus)

enum class AttendanceStatus { PRESENT, LATE, ABSENT }

class StudentReportViewModel : ViewModel() {
    private val _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>> get() = _students

    init {
        _students.value = listOf(
            Student("Yoeun Yous", "yousee@gmail.com", AttendanceStatus.PRESENT),
            Student("Chansea Sim", "chanseasim@gmail.com", AttendanceStatus.PRESENT),
            Student("John Doe", "johndoe@gmail.com", AttendanceStatus.LATE),
            Student("Jane Smith", "janesmith@gmail.com", AttendanceStatus.ABSENT)
        )
    }

    fun updateStatus(position: Int, status: AttendanceStatus) {
        _students.value = _students.value?.toMutableList()?.also {
            it[position].status = status
        }
    }
}
