package com.bbu.attendancetracking.ui.scanqr

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.data.LocalStorageHelper
import com.bbu.attendancetracking.repository.ApiRepository

class ScanQrViewModel : ViewModel() {

    private val _errorMessage = MutableLiveData<String>().apply { value = "" }
    val errorMessage: LiveData<String> = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage

    suspend fun submitAttendant(classId: Int = 0) {
        try {
            Log.d("API CALL:", classId.toString())

            var userId: Int? = LocalStorageHelper.getLoginResponse(MyApplication.instance.applicationContext)?.user?.user_id;
            val response = ApiRepository().submitAttendance(classId, userId?:0)

            if (response.isSuccessful) {
                response.body()?.let {
                    _successMessage.postValue("Submit Attendance Successfully!")
                }
            } else {
                _errorMessage.postValue("Failed to submit attendance: ${response.message()}")
            }
        } catch (e: Exception) {
            _errorMessage.postValue("Error: ${e.localizedMessage}")
        }
    }
}