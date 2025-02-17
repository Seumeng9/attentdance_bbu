package com.bbu.attendancetracking.api

import android.graphics.ColorSpace.Model
import com.bbu.attendancetracking.model.LoginRequest
import com.bbu.attendancetracking.model.LoginResponse

import com.bbu.attendancetracking.ui.signup.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiService {

    @Headers(
        "accept: */*",
        "Content-Type: application/json"
    )

    // Example of a GET request
    @GET("endpoint")
    suspend fun getData(): Response<Model>


    @POST("auth/login")
    suspend fun login(@Body requestBody: LoginRequest): Response<LoginResponse>

    @POST("/auth/register")
    suspend fun register(@Body requestBody: User): Response<String>

//    @GET("classes/list")
//    suspend fun getClassList(@Body requestBody: RequestBody): Response<ClassResponse>



}