package com.bbu.attendancetracking.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    val displayName: String
)

data class LoginRequest(
    val password: String,
    val username: String
)
