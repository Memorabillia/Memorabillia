package com.example.memorabilia.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import com.example.memorabilia.ViewModelFactory
import com.example.memorabilia.databinding.ActivityRegisterBinding
import com.example.memorabilia.login.LoginActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class RegisterActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityRegisterBinding
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val username = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            val jsonObject = JSONObject()
            jsonObject.put("username", username)
            jsonObject.put("email", email)
            jsonObject.put("password", password)

            val requestBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                jsonObject.toString()
            )

            alertDialog = showLoadingDialog("Signing up...")

            val request = Request.Builder()
                .url("https://capstonebangkitc241-ps182.et.r.appspot.com/register")
                .post(requestBody)
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    alertDialog?.dismiss()
                    runOnUiThread {
                        showAlertDialog("Signup failed. Please try again.")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    alertDialog?.dismiss()
                    if (response.isSuccessful) {
                        runOnUiThread {
                            showAlertDialog("You have successfully signed up.")
                            navigateToLogin()
                        }
                    } else {
                        runOnUiThread {
                            showAlertDialog("Signup failed. Please try again.")
                        }
                    }
                }
            })
        }
    }

    private fun showAlertDialog(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoadingDialog(message: String): AlertDialog {
        return AlertDialog.Builder(this)
            .setMessage(message)
            .setCancelable(false)
            .show()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        alertDialog?.dismiss()
        super.onDestroy()
    }
}