package com.example.memorabilia.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.memorabilia.ViewModelFactory
import com.example.memorabilia.data.UserModel
import com.example.memorabilia.databinding.ActivityLoginBinding
import com.example.memorabilia.main.MainActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                alertDialog = showLoadingDialog("Logging in...")
                makeLoginRequest(email, password)
            } else {
                showAlertDialog("Please fill in both email and password.")
            }
        }
    }

    private fun makeLoginRequest(email: String, password: String) {
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("password", password)

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            jsonObject.toString()
        )

        val request = Request.Builder()
            .url("https://capstonebangkitc241-ps182.et.r.appspot.com/login")
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                alertDialog?.dismiss()
                Log.e("LoginActivity", "Login failed: ${e.message}")
                runOnUiThread {
                    showAlertDialog("Login failed. Please try again.")
                }
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                alertDialog?.dismiss()
                val responseBody = response.body?.string()
                Log.d("LoginActivity", "Response body: $responseBody")  // Log the entire response body for debugging
                Log.d("LoginActivity", "Response code: ${response.code}")
                Log.d("LoginActivity", "Response headers: ${response.headers}")

                try {
                    if (response.isSuccessful && responseBody != null) {
                        val jsonObject = JSONObject(responseBody)
                        val accessToken = jsonObject.optString("accessToken", null)
                        val userId = jsonObject.optString("userId", null)

                        if (accessToken != null && userId != null) {
                            viewModel.updateToken(accessToken)
                            val user = UserModel(email, accessToken, true)
                            viewModel.saveSession(user)

                            runOnUiThread {
                                showAlertDialog("You have successfully logged in.")
                                navigateToMainActivity()
                            }
                        } else {
                            val errorMessage = jsonObject.optString("message", "Login failed. Please try again.")
                            Log.e("LoginActivity", "Login failed: $errorMessage")
                            runOnUiThread {
                                showAlertDialog(errorMessage)
                            }
                        }
                    } else {
                        val errorMessage = "Login failed. Server returned an error: ${response.code}"
                        Log.e("LoginActivity", "Login failed: $errorMessage")
                        runOnUiThread {
                            showAlertDialog(errorMessage)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LoginActivity", "Invalid response: ${e.message}")
                    runOnUiThread {
                        showAlertDialog("Invalid response. Please try again.")
                    }
                }
            }
        })
    }


    private fun showAlertDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showLoadingDialog(message: String): AlertDialog {
        return AlertDialog.Builder(this)
            .setMessage(message)
            .setCancelable(false)
            .show()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        alertDialog?.dismiss()
        super.onDestroy()
    }
}
