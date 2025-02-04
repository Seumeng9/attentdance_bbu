package com.bbu.attendancetracking.ui.home.student_report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bbu.attendancetracking.repository.AttendantRepository
import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("userName")
    val name: String,
    val email: String,
    var status: String,
    @SerializedName("user_id")
    var userId: Int
    )

//"email": "string",
//"status": "string",
//"userName": "string",
//"user_id": 0

enum class AttendanceStatus { present, late, absent }

class StudentReportViewModel : ViewModel() {
    private val _students = MutableLiveData<List<Student>>()
    val students: LiveData<List<Student>> get() = _students

//    init {
//        _students.value = listOf(
//            Student("Yoeun Yous", "yousee@gmail.com", AttendanceStatus.present.name, 0),
//            Student("Chansea Sim", "chanseasim@gmail.com", AttendanceStatus.present.name, 0),
//            Student("John Doe", "johndoe@gmail.com", AttendanceStatus.late.name, 0),
//            Student("Jane Smith", "janesmith@gmail.com", AttendanceStatus.absent.name, 0)
//        )
//    }

    fun updateStatus(position: Int, status: String) {
        _students.value = _students.value?.toMutableList()?.also {
            it[position].status = status
        }
    }

    suspend fun getStudentReportByClassIdForTeacher(classId: Int) {
        try {

            val response = AttendantRepository().getAttendanceByClassIdForTeacher(
                classId
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    _students.value = it
                }
            }
        } catch (e: Exception) {
            Log.d("Error Fetch report Class", "$e")
        }finally {
//            _isLoading.value = false
        }
    }
}
