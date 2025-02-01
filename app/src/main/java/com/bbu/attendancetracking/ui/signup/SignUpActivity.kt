package com.bbu.attendancetracking.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.helpers.LocalStorageHelper
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response


class SignupActivity : AppCompatActivity() {

    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        supportActionBar?.hide()

        val emailField = findViewById<EditText>(R.id.email)
        val firstNameField = findViewById<EditText>(R.id.firstName)
        val lastNameField = findViewById<EditText>(R.id.lastName)
        val passwordField = findViewById<EditText>(R.id.password)
        val confirmPassField = findViewById<EditText>(R.id.confirm_pass)
        val phoneField = findViewById<EditText>(R.id.phoneNumber)
        val userField = findViewById<EditText>(R.id.userName)
        val roleGroup = findViewById<RadioGroup>(R.id.roleGroup)
        val signUpButton = findViewById<Button>(R.id.signUp)

        val studentRadio: RadioButton = findViewById(R.id.studentRadio)

        studentRadio.isChecked = true

        signUpButton.setOnClickListener {

            val email = emailField.text.toString()
            val firstName = firstNameField.text.toString()
            val lastName = lastNameField.text.toString()
            val password = passwordField.text.toString()
            val selectedRoleId = roleGroup.checkedRadioButtonId
            val phone = phoneField.text.toString()
            val username = userField.text.toString()
            val selectedRole = if (selectedRoleId != -1) {
                val roleButton = findViewById<RadioButton>(selectedRoleId)
                Role(roleButton.text.toString(), roleButton.tag.toString().toInt())
            } else {
                null
            }

            if (email.isBlank() || firstName.isBlank() || lastName.isBlank() || password.isBlank() || selectedRole == null) {
                Toast.makeText(this, "Please fill all fields and select a role.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(password != confirmPassField.text.toString()) {
                Toast.makeText(this, "Your Password Not Match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            LocalStorageHelper.saveToken(MyApplication.instance.applicationContext, "")

            findViewById<ProgressBar>(R.id.loading)?.visibility = View.VISIBLE

            val user = User(

                email = email,
                firstName = firstName,
                lastName = lastName,
                password = password,
                phone= phone,
                username= username,
//                username = "$firstName.$lastName".lowercase(),
                roles = listOf(selectedRole)
            )

            lifecycleScope.launch {
                try {
                    // Show loading spinner before the call
                    findViewById<ProgressBar>(R.id.loading)?.visibility = View.VISIBLE

                    // Call the registerUser function and wait for it to complete
                    var resp: Response<String>? = signupViewModel.registerUser(user)

                    // Hide the loading spinner after the call completes
                    findViewById<ProgressBar>(R.id.loading)?.visibility = View.GONE

                    // Finish activity after the registration is successful
                    if(resp?.isSuccessful == true){
                        finish()
                    }else {
//                        Log.d("resp signup:", "${resp?.errorBody().toString().}")


                        val errorBody = resp?.errorBody()?.string() // Read the response body as string

                        val message = JSONObject(errorBody).getString("message")


                        Toast.makeText(MyApplication.instance.applicationContext, message?:"Failed to Register, Please Try Again Later", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    // Handle any exceptions
                    findViewById<ProgressBar>(R.id.loading)?.visibility = View.GONE
                    println("Error: ${e.localizedMessage}")
                }
            }


        }

//        signupViewModel.signupStatus.observe(this) { status ->
//            Toast.makeText(this, status, Toast.LENGTH_LONG).show()
//        }
    }
}
