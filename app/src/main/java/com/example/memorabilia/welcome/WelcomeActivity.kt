package com.example.memorabilia.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.memorabilia.databinding.ActivityWelcomeBinding
import com.example.memorabilia.login.LoginActivity
import com.example.memorabilia.register.RegisterActivity


class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        rotateCloud(binding.cloudTopLeft, -1.5f,false)
        rotateCloud(binding.cloudBottomRight, -1.5f,true)
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    private fun rotateCloud(view: View, scaleFactor: Float, reverse: Boolean) {
        val rotationAnimator = if (reverse) {
            ObjectAnimator.ofFloat(view, "rotation", 0f, -360f)
        } else {
            ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        }.apply {
            duration = 10000 // 10 seconds
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
        }

        val scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", scaleFactor).apply {
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            duration = 3000 // 3 seconds
            interpolator = LinearInterpolator()
        }

        val scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", scaleFactor).apply {
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
            duration = 3000 // 3 seconds
            interpolator = LinearInterpolator()
        }

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(rotationAnimator, scaleXAnimator, scaleYAnimator)
        animatorSet.start()
    }




}