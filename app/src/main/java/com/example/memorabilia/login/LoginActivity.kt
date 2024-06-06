package com.example.memorabilia.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.memorabilia.ViewModelFactory
import com.example.memorabilia.data.UserModel
import com.example.memorabilia.databinding.ActivityLoginBinding
import com.example.memorabilia.main.MainActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONException
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
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (!isEmailValid(email) || !isPasswordValid(password)) {
                AlertDialog.Builder(this)
                    .setTitle("Login Failed")
                    .setMessage("Please enter a valid email and password.")
                    .setPositiveButton("OK") { _, _ -> }
                    .show()
                return@setOnClickListener
            }

            makeLoginRequest(email, password)
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

        alertDialog = showLoadingDialog("Logging in...")

        val request = Request.Builder()
            .url("https://capstonebangkitc241-ps182.et.r.appspot.com/login")
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                alertDialog?.dismiss()
                runOnUiThread {
                    showAlertDialog("Login failed. Please try again.")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                alertDialog?.dismiss()
                val responseBody = response.body?.string()

                // Check if the response body is a valid JSON
                try {
                    JSONObject(responseBody)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    runOnUiThread {
                        showAlertDialog("Invalid response. Please try again.")
                    }
                    return
                }

                // Proceed with converting the response to JSONObject and further processing
                val jsonObject = JSONObject(responseBody)

                if (response.isSuccessful) {
                    val loginResult = jsonObject.optJSONObject("loginResult")
                    if (loginResult != null) {
                        val token = loginResult.optString("token")
                        viewModel.updateToken(token)
                        val user = UserModel(email, token, true)
                        viewModel.saveSession(user)

                        runOnUiThread {
                            showAlertDialog("You have successfully logged in.")
                            navigateToMainActivity()
                        }
                    } else {
                        runOnUiThread {
                            showAlertDialog("Login failed. Please try again.")
                        }
                    }
                } else {
                    runOnUiThread {
                        showAlertDialog("Login failed. Please try again.")
                    }
                }
            }
        })
    }



            private fun isEmailValid(email: String): Boolean {
        val emailPattern = "^[A-Za-z\\d+_.-]+@[A-Za-z\\d.-]+$"
        return email.matches(emailPattern.toRegex())
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*\\d).{8,}$"
        return password.matches(passwordPattern.toRegex())
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
