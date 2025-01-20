package com.bbu.attendancetracking.api

import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.data.LocalStorageHelper
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = LocalStorageHelper.getToken(MyApplication.instance.applicationContext)

        // Create a new request by adding the Authorization header on top of the original headers
        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token") // Add, not replace
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}