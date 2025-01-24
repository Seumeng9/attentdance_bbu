package com.bbu.attendancetracking.api

import android.graphics.ColorSpace.Model
import com.bbu.attendancetracking.data.model.AttendanceResponse
import com.bbu.attendancetracking.data.model.ClassResponse
import com.bbu.attendancetracking.data.model.LoginRequest
import com.bbu.attendancetracking.data.model.LoginResponse
import com.google.gson.Gson
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @Headers(
        "accept: */*",
        "Content-Type: application/json"
    )

    // Example of a GET request
    @GET("endpoint")
    suspend fun getData(): Response<Model>

    // Example of a POST request
    @POST("attendance")
    suspend fun submitAttendance(
        @Body requestBody: Map<String, Int>
    ): Response<AttendanceResponse>


    @POST("auth/login")
    suspend fun login(@Body requestBody: LoginRequest): Response<LoginResponse>

//    @GET("classes/list")
//    suspend fun getClassList(@Body requestBody: RequestBody): Response<ClassResponse>

    @GET("classes/list")
    suspend fun getClassList(
        @Query("_limit") limit: String,
        @Query("name") name: String,
        @Query("page") page: Int
    ): Response<ClassResponse>



}