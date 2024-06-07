package com.example.memorabilia.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.memorabilia.R

class AppGuideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_guide)

        val view = findViewById<View>(R.id.sVAppGuide)
        view?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                // your code here
                insets
            }
        } ?: run {
            // Handle the case where the view is null
            Log.e("AppGuideActivity", "View with ID 'sVAppGuide' not found in the layout.")
        }
    }
}
