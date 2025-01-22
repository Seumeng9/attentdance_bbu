package com.bbu.attendancetracking.api

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.ui.login.LoginActivity
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

import android.os.Handler
import android.os.Looper

class ErrorInterceptor() : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 500) {


            Handler(Looper.getMainLooper()).post {
                Toast.makeText(MyApplication.instance.applicationContext, "Session expired. Please log in again.", Toast.LENGTH_LONG).show()
            }//

            val sharedPreferences: SharedPreferences = MyApplication.instance.applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLogin", false)
            editor.apply()
            // Redirect to LoginActivity on 500 status
            val intent = Intent(MyApplication.instance.applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            MyApplication.instance.applicationContext.startActivity(intent)
        }
        return response
    }
}
