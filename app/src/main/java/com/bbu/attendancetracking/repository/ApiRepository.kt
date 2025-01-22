package com.bbu.attendancetracking.repository



import android.graphics.ColorSpace.Model
import android.view.PixelCopy.Request
import com.bbu.attendancetracking.api.ApiClient
import com.bbu.attendancetracking.data.model.ClassResponse
import com.google.gson.Gson
import okhttp3.RequestBody
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

    suspend fun getListClass(
        limit: String,
        filter: String,
        page: Int
    ): Response<ClassResponse> {
        return apiService.getClassList(limit, filter, page)
    }

}
