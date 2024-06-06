package com.example.memorabilia.data

import com.example.memorabilia.api.ApiConfig
import com.example.memorabilia.api.ApiService
import com.example.memorabilia.api.response.LoginResponse
import com.example.memorabilia.api.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Call

class Repository(
    private var apiService: ApiService,
    private val token: String,
    private val userPreference: UserPreference,
) {
    init {
        if (token.isNotEmpty()) {
            this.apiService = ApiConfig.getApiService(token)
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun register(name: String, email: String, password: String): Call<RegisterResponse> {
        return apiService.userRegister(name, email, password)
    }

    fun login(email: String, password: String): Call<LoginResponse> {
        return apiService.userLogin(email, password)
    }

    fun updateToken(token: String) {
        this.apiService = ApiConfig.getApiService(token)
    }

    fun getThemeSetting(): Flow<Boolean> {
        return userPreference.getThemeSetting()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        userPreference.saveThemeSetting(isDarkModeActive)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            token: String = "",
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, token, userPreference)
            }.also { instance = it }
    }
}