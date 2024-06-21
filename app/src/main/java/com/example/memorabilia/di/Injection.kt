package com.example.memorabilia.di

import android.content.Context
import com.example.memorabilia.api.ApiConfig
import com.example.memorabilia.data.Repository
import com.example.memorabilia.data.UserPreference
import com.example.memorabilia.data.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val appContext = context.applicationContext
        val pref = UserPreference.getInstance(appContext.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val repository = Repository.getInstance(apiService, pref, user.token)
        return repository
    }

}
