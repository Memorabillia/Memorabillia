package com.example.memorabilia.theme

import ThemeSwitchDebouncer
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.memorabilia.R
import com.example.memorabilia.ViewModelFactory
import com.example.memorabilia.data.UserPreference
import com.example.memorabilia.data.dataStore
import com.example.memorabilia.di.Injection
import com.google.android.material.switchmaterial.SwitchMaterial

class SwitchThemeActivity : AppCompatActivity() {
    private val themeSwitchDebouncer = ThemeSwitchDebouncer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_theme)

        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        val pref = UserPreference.getInstance(application.dataStore)
        val repository = Injection.provideRepository(this)
        val themeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(repository, pref)
        ).get(ThemeViewModel::class.java)

        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeSwitchDebouncer.debounce {
                themeViewModel.saveThemeSetting(isChecked)
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        themeSwitchDebouncer.cancel()
    }
}
