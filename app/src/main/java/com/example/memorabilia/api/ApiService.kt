package com.example.memorabilia.api

import com.example.memorabilia.api.response.LoginResponse
import com.example.memorabilia.api.response.NewsResponse
import com.example.memorabilia.api.response.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun userRegister(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<LoginResponse>

    @GET("everything")
    suspend fun searchArticles(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>

    @GET("everything")
    suspend fun getArticleDetail(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}