package com.example.memorabilia.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.memorabilia.R
import com.example.memorabilia.ViewModelFactory
import com.example.memorabilia.data.Repository
import com.example.memorabilia.di.Injection
import com.example.memorabilia.main.MainActivity
import com.example.memorabilia.search.SearchActivity
import com.example.memorabilia.theme.SwitchThemeActivity
import com.example.memorabilia.theme.ThemeViewModel
import com.example.memorabilia.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton


class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModels<SettingsViewModel> {
        ViewModelFactory.getInstance(this)
    }


    private lateinit var repository: Repository
    private lateinit var themeViewModel: ThemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        repository = Injection.provideRepository(this)
        themeViewModel = ViewModelProvider(this, ViewModelFactory(this, repository)).get(ThemeViewModel::class.java)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.profilenav

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homenav -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.searchnav -> {
                    startActivity(Intent(applicationContext, SearchActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.profilenav -> {
                    true
                }

                else -> false
            }
        }

        val logoutProfile = findViewById<MaterialButton>(R.id.LogoutButton)
        logoutProfile.setOnClickListener {

            themeViewModel.saveThemeSetting(false)

            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        val themeButton = findViewById<MaterialButton>(R.id.ThemeButton)
        themeButton.setOnClickListener {
            val intent = Intent(this, SwitchThemeActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }
        val appguideButton = findViewById<MaterialButton>(R.id.AppGuideButton)
        appguideButton.setOnClickListener {
            val intent = Intent(this, AppGuideActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        val aboutUsButton = findViewById<MaterialButton>(R.id.AboutButton)
        aboutUsButton.setOnClickListener {
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }
        val logoutButton = findViewById<MaterialButton>(R.id.LogoutButton)
        logoutButton.setOnClickListener {
            // Change the theme to light mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            // Save the theme setting to the data store
            themeViewModel.saveThemeSetting(false)
            // Log out the user
            viewModel.logout()
            // Start the WelcomeActivity
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


    }
}