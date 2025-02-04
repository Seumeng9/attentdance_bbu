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
        fun getLoginResponse(): LoginResponse? {
            val sharedPreferences = MyApplication.instance.applicationContext.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val json = sharedPreferences.getString("loginResponse", null)

            return if (json != null) {
                Gson().fromJson(json, LoginResponse::class.java)
            } else {
                null
            }
        }


        fun getUserRole(): String {

            val userRoles: List<String>  = getLoginResponse()?.user?.roles ?: emptyList()

            return if (userRoles.contains("ADMIN")) "ADMIN"
                else if (userRoles.contains("TEACHER")) "TEACHER"
                else  "STUDENT"

        }

        fun saveToken(context: Context, token: String) {
            val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putString("token", token)
            editor.apply()
        }

        fun  getToken(): String {
            val sharedPreferences = MyApplication.instance.applicationContext.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
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

         fun saveImageUri(uri: String) {
            val sharedPreferences = MyApplication.instance.applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            sharedPreferences.edit()
                .putString("IMAGE_URI", uri)
                .apply()
        }

         fun getImageUri(): String? {
            val sharedPreferences = MyApplication.instance.applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            return sharedPreferences.getString("IMAGE_URI", null)
        }
    }
}