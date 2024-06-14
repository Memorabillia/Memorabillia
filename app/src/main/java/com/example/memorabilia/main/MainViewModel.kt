package com.example.memorabilia.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.memorabilia.api.response.Book
import com.example.memorabilia.data.Repository
import com.example.memorabilia.data.UserModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: Repository,
) : ViewModel() {
    private val _bookRecommendations = MutableLiveData<List<Book>>()
    val bookRecommendations: LiveData<List<Book>> = _bookRecommendations

    fun fetchBookRecommendations() {
        viewModelScope.launch {
            try {
                val recommendations = repository.getBookRecommendations()
                _bookRecommendations.value = recommendations
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching recommendations", e)
            }
        }
    }


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }





}