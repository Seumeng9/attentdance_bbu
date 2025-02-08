package com.bbu.attendancetracking

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bbu.attendancetracking.databinding.ActivityMainBinding
import com.bbu.attendancetracking.helpers.LocalStorageHelper
import com.bbu.attendancetracking.model.LoginResponse
import com.bbu.attendancetracking.ui.login.LoginActivity

import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI


import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Check login state
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val isLogin = sharedPreferences.getBoolean("isLogin", false)

        println("isLogin $isLogin");

        if (!isLogin) {
            // Navigate to LoginActivity if not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Close MainActivity
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        supportActionBar?.hide()  // Hide the default ActionBar

        var loginDetails: LoginResponse? =
            LocalStorageHelper.getLoginResponse()


        Log.d("AAAA", "loginDetails?.user?.roles: ${loginDetails?.user?.roles}")

        binding.navView.setOnItemSelectedListener { item ->
            Log.d("aaaa", "swiitch tab")
            NavigationUI.onNavDestinationSelected(item, binding.navView.findNavController())
            true
        }


        navView.menu.findItem(R.id.navigation_scan_qr).isVisible =
            (loginDetails?.user?.roles?.any { it.equals("STUDENT", ignoreCase = true) } == true)


            val window: Window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor =
                resources.getColor(R.color.colorPrimary) // Set your desired color here


        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_scan_qr,
                R.id.navigation_attendance,
                R.id.navigation_profile
            )
        )
        navView.setupWithNavController(navController)

        requestLocationPersmission()


        // You can add any additional setup for the custom app bar here
    }




    private fun requestLocationPersmission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
    }
}