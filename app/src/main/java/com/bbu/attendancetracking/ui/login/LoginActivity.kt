package com.bbu.attendancetracking.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bbu.attendancetracking.MainActivity
import com.bbu.attendancetracking.MyApplication
import com.bbu.attendancetracking.R
import com.bbu.attendancetracking.data.LocalStorageHelper
import com.bbu.attendancetracking.data.LoginRepository
import com.bbu.attendancetracking.databinding.ActivityLoginBinding
import com.bbu.attendancetracking.ui.signup.SignupActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading

        // Initially set the password to hidden state
        var isPasswordVisible = false

        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide, 0)

        password.setOnTouchListener { _, event ->
            val drawableRight = password.compoundDrawables[2]
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                if (event.rawX >= password.right - drawableRight.bounds.width()) {
                    // Toggle the password visibility
                    isPasswordVisible = !isPasswordVisible
                    if (isPasswordVisible) {
                        password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide, 0)
                    } else {
                        password.transformationMethod = PasswordTransformationMethod.getInstance()
                        password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.hide_unhide, 0)
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Initialize Repository and ViewModel
        val repository = LoginRepository()
        val factory = LoginViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })



        // Observe login result
        loginViewModel.loginResult.observe(this) { result ->

            loading.visibility = View.GONE
            when (result) {
                is com.bbu.attendancetracking.data.Result.Success -> {
                    // Handle success
                    Log.d("LoginActivity", "Login successful: ${result.data}")
                    setResult(Activity.RESULT_OK)

                    val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLogin", true)
                    editor.apply()

                    LocalStorageHelper.saveLoginResponse(MyApplication.instance.applicationContext, result.data)
                    LocalStorageHelper.saveToken(MyApplication.instance.applicationContext, result.data?.access_token ?: "")


                    // Navigate to MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // Close LoginActivity
                }
                is com.bbu.attendancetracking.data.Result.Error -> {
                    // Handle error

                    Toast.makeText(this, "Login Failed: Invalid Username Or Password", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Login failed: ${result.exception.message}")
                }

                else -> {}
            }
        }

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {

                if(username.text.isEmpty() || password.text.isEmpty()) {
                    Toast.makeText(applicationContext, "Please Fill Your Username and Password", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                loading?.visibility = View.VISIBLE

                loginViewModel.login(username.text.toString(), password.text.toString())


            }

            binding.registerLink?.setOnClickListener {
                val intent = Intent(applicationContext, SignupActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
//        Toast.makeText(
//            applicationContext,
//            "$welcome $displayName",
//            Toast.LENGTH_LONG
//        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}