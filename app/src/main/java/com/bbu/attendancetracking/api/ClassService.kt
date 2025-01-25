package com.bbu.attendancetracking.api

import com.bbu.attendancetracking.model.ClassResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ClassService {
    @Headers(
        "accept: */*",
        "Content-Type: application/json"
    )

    @GET("classes/list")
    suspend fun getClassList(
        @Query("_limit") limit: String,
        @Query("name") name: String,
        @Query("page") page: Int
    ): Response<ClassResponse>

}