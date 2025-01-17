package com.bbu.attendancetracking.ui.signup

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bbu.attendancetracking.R


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

            val user = User(
                email = email,
                firstName = firstName,
                lastName = lastName,
                password = password,
                username = "$firstName.$lastName".lowercase(),
                phone = "N/A", // Optional, not provided in UI
                roles = listOf(selectedRole)
            )

            signupViewModel.registerUser(user)
        }

//        signupViewModel.signupStatus.observe(this) { status ->
//            Toast.makeText(this, status, Toast.LENGTH_LONG).show()
//        }
    }
}
