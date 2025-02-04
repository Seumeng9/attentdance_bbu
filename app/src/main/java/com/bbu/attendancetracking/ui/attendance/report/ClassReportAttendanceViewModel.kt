package com.bbu.attendancetracking.ui.attendance.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bbu.attendancetracking.model.ClassItem
import com.bbu.attendancetracking.repository.ClassRepository

class ReportAttendanceViewModel: ViewModel() {

    private val _classList = MutableLiveData<List<ClassItem>>()
    val classList: LiveData<List<ClassItem>> = _classList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


//    init {
//        _classList.value = generateClassItems()
//    }

    suspend fun fetchClasses() {

        if (classList.value?.isNotEmpty() == true){
            return
        }

        _isLoading.value = true

        try {
            val response = ClassRepository().getAllClass()

            if (response.isSuccessful) {
                response.body()?.let {
                    _classList.value = response.body()
                }
            }
        } catch (e: Exception) {
            Log.d("Error Fetch All Class", "Error")
        }finally {
            _isLoading.value = false
        }
    }



    private fun generateClassItems(): List<ClassItem> {
        return listOf(
            ClassItem(1, "Math 101", "Basic Math Class", "Active", "Lab A", "08:00 AM", "2025-02-01", "Alice", "Johnson", "U001"),
            ClassItem(2, "Physics 101", "Intro to Physics", "Active", "Lab B", "09:00 AM", "2025-02-02", "Bob", "Smith", "U002"),
            ClassItem(3, "Chemistry 101", "Intro to Chemistry", "Inactive", "Lab C", "10:00 AM", "2025-02-03", "Charlie", "Brown", "U003"),
            ClassItem(4, "Biology 101", "Basic Biology", "Active", "Lab D", "11:00 AM", "2025-02-04", "David", "Williams", "U004"),
            ClassItem(5, "History 101", "World History Overview", "Active", "Lab E", "01:00 PM", "2025-02-05", "Emma", "Davis", "U005"),
            ClassItem(6, "Computer Science 101", "Intro to Programming", "Active", "Lab F", "02:00 PM", "2025-02-06", "Frank", "Miller", "U006"),
            ClassItem(7, "Art 101", "Introduction to Art", "Inactive", "Lab G", "03:00 PM", "2025-02-07", "Grace", "Wilson", "U007"),
            ClassItem(8, "Music 101", "Basics of Music", "Active", "Lab H", "04:00 PM", "2025-02-08", "Hank", "Moore", "U008"),
            ClassItem(9, "Literature 101", "Intro to Literature", "Active", "Lab I", "05:00 PM", "2025-02-09", "Ivy", "Taylor", "U009")
        )
    }



}