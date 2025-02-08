package com.bbu.attendancetracking.model.report

import com.google.gson.annotations.SerializedName

data class ClassAttendanceReport (
    @SerializedName("class_id")
    val classId: Int,
    val title: String,
    val description: String,
    val status: String,
    val labNo: String,
    val scheduled: String,
    val date: String,
    @SerializedName("attendance_his")
    val attendanceHis: List<AttendanceHistory>
)


data class AttendanceHistory(
    val date: String,
    val status: String
)
