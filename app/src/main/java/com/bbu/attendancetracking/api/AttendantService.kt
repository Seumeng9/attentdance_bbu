package com.bbu.attendancetracking.api

import com.bbu.attendancetracking.model.AttendanceResponse
import com.bbu.attendancetracking.model.report.ClassAttendanceReport
import com.bbu.attendancetracking.ui.home.student_report.Student
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AttendantService {
    @Headers(
        "accept: */*",
        "Content-Type: application/json"
    )


    // Example of a POST request
    @POST("attendance")
    suspend fun submitAttendance(
        @Body requestBody: Map<String, Int>,
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,

        ): String

    @GET("/attendance/getAttendanceHistoryByClass")
    suspend fun getAttHisByClassId(

        @Query("classId") classId: Int,
        @Query("month") month: Int,
        @Query("year") year: Int

    ): Response<ClassAttendanceReport>



    @GET("/classes/getAttendanceTodayListByClassId")
    suspend fun getAttendanceByClassIdForTeacher(
        @Query("classId") classId: Int
    ): Response<List<Student>>


}