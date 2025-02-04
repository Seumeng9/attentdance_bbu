package com.bbu.attendancetracking.api

import com.bbu.attendancetracking.model.ClassCategory
import com.bbu.attendancetracking.model.ClassItem
//import com.bbu.attendancetracking.model.ClassResponse
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
        @Query("Authorization") token: String,
        @Query("params") searchText: String,
        @Query("requestBy") role: String
    ): Response<List<ClassCategory>>

    @GET("/classes")
    suspend fun getAllClass(): Response<List<ClassItem>>



}