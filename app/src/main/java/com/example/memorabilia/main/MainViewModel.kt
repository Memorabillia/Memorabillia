package com.example.memorabilia.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.memorabilia.data.Repository
import com.example.memorabilia.data.UserModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository
) : ViewModel() {


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }



}