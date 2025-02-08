package com.bbu.attendancetracking.repository

import com.bbu.attendancetracking.api.ApiClient
import com.bbu.attendancetracking.model.ClassCategory
import com.bbu.attendancetracking.model.ClassItem
//import com.bbu.attendancetracking.model.ClassResponse
import retrofit2.Response

class ClassRepository {

    private val classService = ApiClient.classService

    suspend fun getListClass(
        token: String,
        searchText: String,
        role: String
    ): Response<List<ClassCategory>> {
        return classService.getClassList(token, searchText, role)
    }

    suspend fun getAllClass(): Response<List<ClassItem>> {
        return  classService.getAllClass()
    }
}