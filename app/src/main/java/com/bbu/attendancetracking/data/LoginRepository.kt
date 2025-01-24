package com.bbu.attendancetracking.data

import android.util.Log
import com.bbu.attendancetracking.api.ApiClient
import com.bbu.attendancetracking.data.model.LoggedInUser
import com.bbu.attendancetracking.data.model.LoginRequest
import com.bbu.attendancetracking.data.model.LoginResponse
import okhttp3.ResponseBody

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository() {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

//    fun logout() {
//        user = null
//        dataSource.logout()
//    }

//    suspend fun login(username: String, password: String): Result<LoggedInUser> {
//        // handle login
//        val result = dataSource.login(username, password)
//
//        ApiClient.apiService.login(LoginRequest(password, username))
//
//        if (result is Result.Success) {
//            setLoggedInUser(result.data)
//        }
//
//        return result
//    }

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(password, username)

            Log.d("YourTag", "call api")
            Log.d("YourRepository", "Making API call with request: $request")
            val response = ApiClient.apiService.login(request)

            Log.d("YourRepository", "Raw response: ${response.raw()}") // Logs raw response details
            Log.d("YourRepository", "Response body: ${response.body()}") // Logs parsed body (if successful)

            Log.d("MyRepository", "API response received: $response")
            if (response.isSuccessful) {
                Result.Success(response.body()!!)

            } else {

                Result.Error(Exception("Network request failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}