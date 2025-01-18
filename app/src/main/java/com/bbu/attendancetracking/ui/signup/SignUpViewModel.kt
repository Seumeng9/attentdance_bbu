package com.bbu.attendancetracking.ui.signup


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupViewModel : ViewModel() {

    private val _signupStatus = MutableLiveData<String>()
    val signupStatus: LiveData<String> get() = _signupStatus

    fun registerUser(user: User) {
        // Here you can integrate with your backend service or database.
        // For demonstration, we'll simply print the user data and simulate a successful response.
        println("Registering user: $user")

        // Simulate success response
        _signupStatus.value = "User ${user.username} successfully registered!"
    }
}
