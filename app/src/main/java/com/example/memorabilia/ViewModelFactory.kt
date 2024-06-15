package com.example.memorabilia

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.memorabilia.data.Repository
import com.example.memorabilia.data.UserPreference
import com.example.memorabilia.data.dataStore
import com.example.memorabilia.di.Injection
import com.example.memorabilia.login.LoginViewModel
import com.example.memorabilia.main.MainViewModel
import com.example.memorabilia.register.RegisterViewModel
import com.example.memorabilia.settings.SettingsViewModel
import com.example.memorabilia.theme.ThemeViewModel

class ViewModelFactory(private val repository: Repository, private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ThemeViewModel::class.java) -> {
                ThemeViewModel(pref) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val appContext = context.applicationContext
                    val pref = UserPreference.getInstance(appContext.dataStore)
                    val repository = Injection.provideRepository(context)
                    INSTANCE = ViewModelFactory(repository, pref)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
