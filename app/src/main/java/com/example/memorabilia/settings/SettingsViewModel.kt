package com.example.memorabilia.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memorabilia.data.Repository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: Repository
) : ViewModel() {


    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}