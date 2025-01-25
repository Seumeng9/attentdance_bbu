package com.bbu.attendancetracking.repository

import com.bbu.attendancetracking.api.ApiClient
import com.bbu.attendancetracking.model.AttendanceResponse
import retrofit2.Response

class AttendantRepository {
private final val attendantService = ApiClient.attendantService

    //    "classes_id": 0,
    //    "user_id": 0

    suspend fun submitAttendance(classId: Int, userId: Int): Response<AttendanceResponse> {

        val requestBody = mapOf(
            "classes_id" to classId,
            "user_id" to userId
        )

        return attendantService.submitAttendance(requestBody)
    }
}