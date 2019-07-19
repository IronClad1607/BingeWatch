package com.example.bingewatch.auth

data class AuthRequestToken(
    val username: String,
    val password: String,
    val request_token: String?
)