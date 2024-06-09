package com.example.memorabilia.theme

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.memorabilia.R
import com.example.memorabilia.ViewModelFactory
import com.example.memorabilia.data.Repository
import com.example.memorabilia.di.Injection
import com.google.android.material.switchmaterial.SwitchMaterial


class SwitchThemeActivity : AppCompatActivity() {

    private lateinit var switchTheme: SwitchMaterial
    private lateinit var repository: Repository
    private lateinit var themeViewModel: ThemeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        repository = Injection.provideRepository(this)
        themeViewModel = ViewModelProvider(this, ViewModelFactory(this, repository)).get(ThemeViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_theme)

        switchTheme = findViewById(R.id.switch_theme)

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            switchTheme.isChecked = isDarkModeActive
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            themeViewModel.saveThemeSetting(isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}
