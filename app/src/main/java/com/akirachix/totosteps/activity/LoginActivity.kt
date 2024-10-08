
package com.akirachix.totosteps.activity

//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.Observer
//import com.akirachix.totosteps.activity.viewModel.LoginViewModel
//import com.akirachix.totosteps.R
//import com.akirachix.totosteps.databinding.ActivityLoginBinding
//
//
//class LoginActivity : AppCompatActivity() {
//    lateinit var binding: ActivityLoginBinding
//
//
//
//
//    private val loginViewModel: LoginViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.btnSignUp.setOnClickListener {
//            val intent = Intent(this, ChildAccountActivity::class.java)
//            startActivity(intent)
//
//
//        }
//        supportActionBar?.hide()
//
//
//
//        // Observe login result
//        loginViewModel.loginResult.observe(this, Observer { result ->
//            result.onSuccess {
//                navigateToMain()
//            }.onFailure { throwable ->
//                showError(throwable.localizedMessage ?: "Login failed.")
//            }
//        })
//
//
//
//        // Button click listeners
//        binding.btnSignUp.setOnClickListener { handleEmailLogin() }
//        binding.tvLogin.setOnClickListener { navigateToSignUp() }
//    }
//
//    // Handle email login
//    private fun handleEmailLogin() {
//        val username = binding.etEmail.text.toString().trim()
//        val password = binding.etPassword.text.toString().trim()
//
//        if (loginViewModel.validateForm(username, password)) {
//            loginViewModel.login(username, password)
//        } else {
//            showToast("Please enter both username and password")
//        }
//    }
//
//
//
//
//
//    // Navigate to the main activity
//    private fun navigateToMain() {
//        startActivity(Intent(this, ChildAccountActivity::class.java))
//        finish()
//    }
//
//    // Navigate to the sign-up screen
//    private fun navigateToSignUp() {
//        startActivity(Intent(this, SignupActivity::class.java))
//    }
//
//    // Show error message
//    private fun showError(message: String) {
//        Log.w(TAG, message)
//        showToast(message)
//    }
//
//    // Show a toast message
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//
//    companion object {
//        private const val TAG = "SignInActivity" // Consistent TAG name
//        private const val RC_SIGN_IN = 9001
//    }
//
//}

//The part that works with the PID

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.akirachix.totosteps.activity.viewModel.LoginViewModel
import com.akirachix.totosteps.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Observe login result
        loginViewModel.loginResult.observe(this, Observer { result ->
            result.onSuccess { loginResponse ->
                val userId = loginResponse.user.user_id
                Log.d("LoginActivity", "Login successful. User ID: $userId")
                saveUserIdToSharedPreferences(userId)
                showToast("Login successful")
                navigateToChildAccount()
            }.onFailure { throwable ->
                Log.e("LoginActivity", "Login failed: ${throwable.message}")
                showError(throwable.localizedMessage ?: "Login failed.")
            }
        })

        // Button click listeners
        binding.btnSignUp.setOnClickListener { handleEmailLogin() }
        binding.tvLogin.setOnClickListener { navigateToSignUp() }
    }

    private fun handleEmailLogin() {
        val username = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (loginViewModel.validateForm(username, password)) {
            loginViewModel.login(username, password)
        } else {
            showToast("Please enter both username and password")
        }
    }

    private fun saveUserIdToSharedPreferences(userId: Int) {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putInt("USER_ID", userId).apply()
        Log.d("LoginActivity", "User ID saved to SharedPreferences: $userId")
    }

    private fun navigateToChildAccount() {
        Log.d("LoginActivity", "Navigating to ChildAccountActivity")
        val intent = Intent(this, ChildAccountActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun navigateToSignUp() {
        startActivity(Intent(this, SignupActivity::class.java))
    }

    private fun showError(message: String) {
        Log.e("LoginActivity", "Error: $message")
        showToast(message)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

