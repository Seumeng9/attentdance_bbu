package com.bbu.attendancetracking.ui.attendance.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bbu.attendancetracking.helpers.LocalStorageHelper
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
            val token = LocalStorageHelper.getToken()

            val role = LocalStorageHelper.getUserRole()

            val response = ClassRepository().getListClass(token, "", role)

            if (response.isSuccessful) {
                response.body()?.let {


                    val allClasses = response.body()?.find { it.cal == "All Classes" }?.rec

                    _classList.value = allClasses!!
                }
            }
        } catch (e: Exception) {
            Log.d("Error Fetch All Class", "Error")
        }finally {
            _isLoading.value = false
        }
    }




}