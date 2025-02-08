package com.bbu.attendancetracking.model

import com.google.gson.annotations.SerializedName


//data class ClassResponse(
//    val list: List<ClassCategory>,
//    val pagination: Pagination
//)


data class ClassCategory(
    @SerializedName("classes")
    val rec: List<ClassItem>,
    @SerializedName("title")
    val cal: String
)

data class ClassItem(
    @SerializedName("class_id") val classId: Int,
    val title: String,
    val description: String,
    val status: String,
    val labNo: String,
    val scheduled: String,
    val date: String,
    val firstNm: String,
    val lastNm: String,
    val userId: String
)


data class Pagination(
    val numberOfElements: Int,
    val number: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean
)