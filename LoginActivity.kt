package com.dbsnetwork.iptv

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dbsnetwork.iptv.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginService: LoginService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the login service
        val retrofit = RetrofitHelper.getInstance()
        loginService = retrofit.create(LoginService::class.java)

        // Set up the login button click listener
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Perform login
            performLogin(username, password)
        }
    }

    private fun performLogin(username: String, password: String) {
        binding.loadingProgressBar.visibility = View.VISIBLE
        binding.loginButton.isEnabled = false
        binding.errorMessageTextView.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val loginRequest = LoginRequest(username, password)
                val response = withContext(Dispatchers.IO) {
                    loginService.login(loginRequest)
                }

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        if (loginResponse.success){
                            loginResponse.token?.let { saveLoginToken(it) }
                            Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                            navigateToChannelListActivity()
                        } else {
                            loginResponse.message?.let { showLoginError(it) }
                            Log.e("LoginActivity", "Login failed in API, code is ${loginResponse.message}")

                        }

                    } else {
                        showLoginError("Unexpected response from server")
                        Log.e("LoginActivity", "Login successful, but response body is null")
                    }
                } else {
                    showLoginError("Login failed: ${response.code()}")
                    Log.e("LoginActivity", "Login failed in API, code is ${response.code()}")
                }
            } catch (e: Exception) {
                showLoginError("Network error: ${e.message ?: "Unknown error"}")
                Log.e("LoginActivity", "Login error: ${e.message ?: "Unknown error"}")
            } finally {
                binding.loadingProgressBar.visibility = View.GONE
                binding.loginButton.isEnabled = true
            }
        }
    }

    private fun saveLoginToken(token: String) {
        val sharedPrefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        sharedPrefs.edit().putString("AUTH_TOKEN", token).apply()
    }

    private fun navigateToChannelListActivity() {
        val intent = Intent(this, ChannelListActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoginError(message: String) {
        binding.errorMessageTextView.text = message
        binding.errorMessageTextView.visibility = View.VISIBLE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

