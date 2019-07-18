package com.example.bingewatch.auth.models

data class SessionResponse(
    var success: Boolean,
    val session_id: String
)