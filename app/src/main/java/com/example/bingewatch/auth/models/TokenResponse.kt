package com.example.bingewatch.auth.models

data class TokenResponse(
    var success: Boolean,
    val expires_at: String,
    val request_token: String
)