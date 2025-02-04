package com.bbu.attendancetracking.repository

import android.util.Log
import com.bbu.attendancetracking.api.ApiClient
import com.bbu.attendancetracking.model.AttendanceResponse
import com.bbu.attendancetracking.model.report.ClassAttendanceReport
import com.bbu.attendancetracking.ui.home.student_report.Student
import retrofit2.Response
import retrofit2.http.Query

class AttendantRepository {
private final val attendantService = ApiClient.attendantService

    //    "classes_id": 0,
    //    "user_id": 0

    suspend fun submitAttendance(classId: Int, userId: Int, lat: String, long: String): String {

        val requestBody = mapOf(
            "classes_id" to classId,
            "user_id" to userId
        )


        var r = attendantService.submitAttendance(requestBody,lat, long )


        Log.d("Resp: ", r)


        return r
    }

    suspend fun getAttendanceHis(classId: Int, month: Int, year: Int): Response<ClassAttendanceReport> {
        return attendantService.getAttHisByClassId(
            classId, month, year
        )
    }


    suspend fun getAttendanceByClassIdForTeacher(classId: Int): Response<List<Student>> {
        return attendantService.getAttendanceByClassIdForTeacher(classId)
    }
}