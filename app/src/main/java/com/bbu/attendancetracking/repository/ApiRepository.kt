package com.bbu.attendancetracking.repository



import android.graphics.ColorSpace.Model
import com.bbu.attendancetracking.api.ApiClient
import com.bbu.attendancetracking.model.AttendanceResponse
import retrofit2.Response

class ApiRepository {

    private val apiService = ApiClient.apiService

    // Function to make a GET request
    suspend fun getData(): Response<Model> {
        return apiService.getData()
    }

    // Function to make a POST request
//    suspend fun postData(requestBody: Model): Response<Model> {
//        return apiService.po(requestBody)
//    }





}
