package com.bbu.attendancetracking.ui.signup


data class User(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val phone: String,
    val username: String,
    val roles: List<Role>
)

data class Role(
    val role: String,
    val role_id: Int
)
