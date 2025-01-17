package com.bbu.attendancetracking.api

import android.graphics.ColorSpace.Model
import com.bbu.attendancetracking.data.model.LoginRequest
import okhttp3.ResponseBody
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

    // Example of a POST request
    @POST("endpoint")
    suspend fun postData(@Body requestBody: Model): Response<Model>


    @POST("auth/login")
    suspend fun login(@Body requestBody: LoginRequest): Response<ResponseBody>
}