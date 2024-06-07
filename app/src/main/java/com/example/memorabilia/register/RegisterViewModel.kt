package com.example.memorabilia.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memorabilia.api.response.RegisterResponse
import com.example.memorabilia.data.Repository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun register(username: String, email: String, password: String, callback: (RegisterResponse?) -> Unit) {
        viewModelScope.launch {
            repository.register(username, email, password).enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    callback(response.body())
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    callback(null)
                }
            })
        }
    }

}