package com.bbu.attendancetracking.model

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

data class LoginResponse(
    val status_code: Int? = null,
    val status: String? = null,
    val access_token: String? = null,
    val token_type: String? = null,
    val token_ini: String? = null,
    val token_exp: String? = null,
    val refresh_token: String? = null,
    val user: User? = null
)

data class User(
    val user_id: Int? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val roles: List<String>? = null
)
