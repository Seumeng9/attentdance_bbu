package com.bbu.attendancetracking.api

import android.util.Log
import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.helpers.LocalStorageHelper
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()



        val token = LocalStorageHelper.getToken()

        Log.d("Auth INtercep", "$token")

        // Create a new request by adding the Authorization header on top of the original headers
        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "$token") // Add, not replace
                .addHeader("accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .build()
        } else {
            originalRequest
        }

        Log.d("Auth Header",newRequest.headers.toString() )

        return chain.proceed(newRequest)
    }
}