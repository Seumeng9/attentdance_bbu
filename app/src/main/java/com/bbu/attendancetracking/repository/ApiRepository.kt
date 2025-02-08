package com.bbu.attendancetracking.repository


import com.bbu.attendancetracking.api.ApiClient
import com.bbu.attendancetracking.ui.signup.User

import retrofit2.Response

class ApiRepository {

    private val apiService = ApiClient.apiService


    suspend fun register(user: User): Response<String> {
        return apiService.register(user)
    }

}
