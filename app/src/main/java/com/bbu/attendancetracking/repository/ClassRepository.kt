package com.bbu.attendancetracking.repository

import com.bbu.attendancetracking.api.ApiClient
import com.bbu.attendancetracking.model.ClassResponse
import retrofit2.Response

class ClassRepository {

    private val classService = ApiClient.classService

    suspend fun getListClass(
        limit: String,
        filter: String,
        page: Int
    ): Response<ClassResponse> {
        return classService.getClassList(limit, filter, page)
    }
}