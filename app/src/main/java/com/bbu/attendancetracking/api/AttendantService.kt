package com.bbu.attendancetracking.api

import com.bbu.attendancetracking.model.AttendanceResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AttendantService {
    @Headers(
        "accept: */*",
        "Content-Type: application/json"
    )


    // Example of a POST request
    @POST("attendance")
    suspend fun submitAttendance(
        @Body requestBody: Map<String, Int>
    ): Response<AttendanceResponse>
}