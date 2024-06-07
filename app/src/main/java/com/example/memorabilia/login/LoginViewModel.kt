package com.example.memorabilia.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memorabilia.api.response.LoginResponse
import com.example.memorabilia.data.Repository
import com.example.memorabilia.data.UserModel
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class LoginViewModel(private val repository: Repository) : ViewModel() {
    fun login(email: String, password: String, callback: (Response<LoginResponse>?) -> Unit) {
        viewModelScope.launch {
            try {
                val call = repository.login(email, password)
                call.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        callback(response)
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        callback(null)
                    }
                })
            } catch (e: Exception) {
                callback(null)
            }
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
