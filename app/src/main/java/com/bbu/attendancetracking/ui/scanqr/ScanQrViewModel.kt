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
import com.bbu.attendancetracking.data.model.AttendanceResponse
import com.bbu.attendancetracking.repository.ApiRepository
import com.google.gson.Gson
import kotlin.math.log

class ScanQrViewModel : ViewModel() {

    private val _errorMessage = MutableLiveData<String>().apply { value = "" }
    val errorMessage: LiveData<String> = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage

    suspend fun submitAttendant(classId: Int = 0, lat: String, long: String) {
        try {
            Log.d("API CALL:", classId.toString())

            var userId: Int? = LocalStorageHelper.getLoginResponse(MyApplication.instance.applicationContext)?.user?.user_id;

            val response = ApiRepository().submitAttendance(classId, userId?:0, lat, long)

            try {

                Gson().fromJson(response, AttendanceResponse::class.java)
                _successMessage.postValue("You Are Submitted Attendance Successfully!")


            }catch (e: Exception){
                _errorMessage.postValue("$response")
            }



//            if (response.isSuccessful) {
//                response.body()?.let {
//                    _successMessage.postValue(response.body())
//                }
//            } else {
//                _errorMessage.postValue("Failed to submit attendance: ${response.message()}")
//            }
        } catch (e: Exception) {
            Log.d("Error: ", e.localizedMessage)
            _errorMessage.postValue("Error: ${e.localizedMessage}")
        }
    }
}