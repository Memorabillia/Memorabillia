package com.example.memorabilia.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.memorabilia.data.Repository
import com.example.memorabilia.data.UserPreference
import kotlinx.coroutines.launch

class ThemeViewModel(private val repository: Repository) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return repository.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            repository.saveThemeSetting(isDarkModeActive)
        }
    }
}