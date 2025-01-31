package com.bbu.attendancetracking.repository

import android.util.Log
import com.bbu.attendancetracking.api.ApiClient
import com.bbu.attendancetracking.model.AttendanceResponse
import retrofit2.Response

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
}