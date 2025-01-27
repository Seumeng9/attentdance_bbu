package com.bbu.attendancetracking.repository



import android.graphics.ColorSpace.Model
import android.util.Log
import android.view.PixelCopy.Request
import com.bbu.attendancetracking.api.ApiClient
import com.bbu.attendancetracking.data.model.ApiResponseString
import com.bbu.attendancetracking.data.model.AttendanceResponse
import com.bbu.attendancetracking.data.model.ClassResponse
import com.bbu.attendancetracking.ui.signup.User
import com.google.gson.Gson
import okhttp3.RequestBody
import retrofit2.Response

class ApiRepository {

    private val apiService = ApiClient.apiService


    suspend fun getListClass(
        limit: String,
        filter: String,
        page: Int
    ): Response<ClassResponse> {
        return apiService.getClassList(limit, filter, page)
    }
    

    suspend fun submitAttendance(classId: Int, userId: Int, lat: String, long: String): String {

        val requestBody = mapOf(
            "classes_id" to classId,
            "user_id" to userId
        )

        var r = apiService.submitAttendance(requestBody,lat, long )

        Log.d("Resp: ", r)


        return r
    }

    suspend fun register(user: User): Response<String> {
        return apiService.register(user)
    }

}
