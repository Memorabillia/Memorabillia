package com.example.memorabilia.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.memorabilia.ViewModelFactory
import com.example.memorabilia.databinding.ActivityRegisterBinding
import com.example.memorabilia.login.LoginActivity

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
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (!isEmailValid(email) || !isPasswordValid(password)) {
                AlertDialog.Builder(this)
                    .setTitle("Signup Failed")
                    .setMessage("Please enter a valid email and password.")
                    .setPositiveButton("OK") { _, _ -> }
                    .show()
                return@setOnClickListener
            }

            viewModel.register(name, email, password) { response ->
                if (response != null && !response.error) {
                    AlertDialog.Builder(this)
                        .setTitle("Signup Successful")
                        .setMessage("You have successfully signed up.")
                        .setPositiveButton("OK") { _, _ -> }
                        .show()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    alertDialog?.dismiss()
                    finish()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Signup Failed")
                        .setMessage("Signup failed. Please try again.")
                        .setPositiveButton("OK") { _, _ -> }
                        .show()
                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "^[A-Za-z\\d+_.-]+@[A-Za-z\\d.-]+$"
        return email.matches(emailPattern.toRegex())
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Z])(?=.*\\d).{8,}$"
        return password.matches(passwordPattern.toRegex())
    }
}
