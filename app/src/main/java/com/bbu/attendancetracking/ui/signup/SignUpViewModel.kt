package com.bbu.attendancetracking.ui.signup


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.repository.ApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.EMPTY_RESPONSE
import retrofit2.Response
import retrofit2.Response.success

class SignupViewModel : ViewModel() {

    private val _signupStatus = MutableLiveData<String>()
    val signupStatus: LiveData<String> get() = _signupStatus

//    fun registerUser(user: User) {
//        // Here you can integrate with your backend service or database.
//        // For demonstration, we'll simply print the user data and simulate a successful response.
//        println("Registering user: $user")
//
//        // Simulate success response
//        _signupStatus.value = "User ${user.username} successfully registered!"
//    }

    suspend fun registerUser(user: User): Response<String>? {

            try {
                val response = ApiRepository().register(user)


                return if (response.isSuccessful) {
                    // Handle successful response
//                    println("User registered successfully: ${response.body()}")
                    response
                } else {
                    // Handle error response
//                    println("Failed to register user: ${response.errorBody()?.string()}")


                    response
                }
            } catch (e: Exception) {
                // Handle network or unexpected errors
                println("Error: ${e.localizedMessage}")
                return null
            }
    }
}
