package com.example.memorabilia.data

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)