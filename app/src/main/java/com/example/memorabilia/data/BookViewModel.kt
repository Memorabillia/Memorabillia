package com.example.memorabilia.data

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class BookViewModel(private val repository: Repository,    private val userPreference: UserPreference
) : ViewModel()
