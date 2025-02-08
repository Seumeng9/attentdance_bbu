package com.bbu.attendancetracking.ui.attendance.calendar

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bbu.attendancetracking.api.AttendantService
import com.bbu.attendancetracking.model.ClassItem
import com.bbu.attendancetracking.model.report.AttendanceHistory
import com.bbu.attendancetracking.model.report.ClassAttendanceReport
import com.bbu.attendancetracking.repository.AttendantRepository
import com.bbu.attendancetracking.repository.ClassRepository
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarViewModel: ViewModel() {

    var classId: Int = 0

//    private val _classAttendance = MutableLiveData<ClassAttendanceReport>()
//    val classAttendance: LiveData<ClassAttendanceReport> = _classAttendance

    private val _presentDatesList = MutableLiveData<List<CalendarDay>>()
    val presentDatesList: LiveData<List<CalendarDay>> = _presentDatesList

    private val _lateDatesList = MutableLiveData<List<CalendarDay>>()
    val lateDatesList: LiveData<List<CalendarDay>> = _lateDatesList

    private val _absentDatesList = MutableLiveData<List<CalendarDay>>()
    val absentDatesList: LiveData<List<CalendarDay>> = _absentDatesList


    private val _triggerClear = MutableLiveData<Boolean>(false)
    val triggerClear: LiveData<Boolean> = _triggerClear
    

     fun mapData(apiResponse: List<AttendanceHistory> ) {

         val today = LocalDate.now() // Get today's date

         val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Adjust pattern as per your date format


         val filteredList = apiResponse.filter {
             LocalDate.parse(it.date, formatter) <= today
         }


        // Convert API Data to CalendarDay Lists


        val presentDates = mutableListOf<CalendarDay>()
        val lateDates = mutableListOf<CalendarDay>()
        val absentDates = mutableListOf<CalendarDay>()

        for (attendance in filteredList) {
            val localDate = LocalDate.parse(attendance.date, formatter)
            val calendarDay = CalendarDay.from(localDate.year, localDate.monthValue - 1, localDate.dayOfMonth)

            when (attendance.status.lowercase()) {
                "present" -> presentDates.add(calendarDay)
                "late" -> lateDates.add(calendarDay)
                "absent" -> absentDates.add(calendarDay)
            }
        }

        _presentDatesList.value = presentDates.toList()
        _lateDatesList.value = lateDates.toList()
        _absentDatesList.value = absentDates.toList()

// Print Output (Debugging)
        println("Present: $presentDatesList")
        println("Late: $lateDatesList")
        println("Absent: $absentDatesList")
    }


    fun triggerClear() {
        _triggerClear.value = !_triggerClear.value!!
    }


    suspend fun getAttendanceHistory(classId: Int, month: Int, year: Int) {
        try {

            val response = AttendantRepository().getAttendanceHis(
                classId, month, year
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    mapData(it.attendanceHis)
                }
            }
        } catch (e: Exception) {
            Log.d("Error Fetch All Class", "Error")
        }finally {
//            _isLoading.value = false
        }
    }








}