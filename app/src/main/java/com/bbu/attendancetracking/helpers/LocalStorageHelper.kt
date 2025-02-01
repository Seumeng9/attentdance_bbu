package com.bbu.attendancetracking.helpers

import android.content.Context
import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.model.LoginResponse
import com.google.gson.Gson

class LocalStorageHelper {

    companion object {
        fun saveLoginResponse(context: Context, loginResponse: LoginResponse) {
            val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            // Convert model to JSON string
            val json = Gson().toJson(loginResponse)
            editor.putString("loginResponse", json)
            editor.apply()
        }

        // Retrieve LoginResponse from SharedPreferences
        fun getLoginResponse(context: Context): LoginResponse? {
            val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val json = sharedPreferences.getString("loginResponse", null)

            return if (json != null) {
                Gson().fromJson(json, LoginResponse::class.java)
            } else {
                null
            }
        }

        fun saveToken(context: Context, token: String) {
            val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString("token", token)
            editor.apply()
        }

        fun  getToken(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", "")

            return token?:""
        }

        fun saveGeneratedQrClassId(id: Int) {
            val sharedPreferences = MyApplication.instance.applicationContext.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("classGeneratedQrId", id)
            editor.apply()
        }

        fun  getGeneratedQrClassId(): Int {
            val sharedPreferences = MyApplication.instance.applicationContext.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val id = sharedPreferences.getInt("classGeneratedQrId", 0)

            return id
        }
    }
}