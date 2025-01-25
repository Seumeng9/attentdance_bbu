package com.bbu.attendancetracking.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RecyclerViewItem {
    @Parcelize
    data class Header(val title: String) : RecyclerViewItem(), Parcelable

    @Parcelize
    data class ClassItem(
        val classId: Int,
        val title: String,
        val labNo: String,
        val scheduled: String
    ) : RecyclerViewItem(), Parcelable
}
