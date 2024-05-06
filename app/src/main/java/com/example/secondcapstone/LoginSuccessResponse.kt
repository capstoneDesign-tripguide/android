package com.example.secondcapstone

data class LoginSuccessResponse(
    val accessToken: String,
    val refreshToken: String,
    val loginSuccess: Boolean
)
