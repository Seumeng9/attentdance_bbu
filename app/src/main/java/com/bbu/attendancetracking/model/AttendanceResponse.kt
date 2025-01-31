package com.bbu.attendancetracking.model

data class AttendanceResponse(
    val attendance_id: Int,
    val user_id: Int,
    val classes_id: Int,
    val classes_title: String,
    val first_nm: String,
    val last_nm: String
)


data class ApiResponseString(val message: String)
