package com.example.memorabilia.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memorabilia.api.response.LoginResponse
import com.example.memorabilia.data.Repository
import com.example.memorabilia.data.UserModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: Repository) : ViewModel() {
    fun login(email: String, password: String, callback: (LoginResponse?) -> Unit) {
        viewModelScope.launch {
            repository.login(email, password).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    callback(response.body())
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    callback(null)
                }
            })
        }
    }
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    fun updateToken(token: String) {
        viewModelScope.launch {
            repository.updateToken(token)
        }
    }
}