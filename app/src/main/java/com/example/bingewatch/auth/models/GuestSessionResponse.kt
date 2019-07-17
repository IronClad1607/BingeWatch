package com.example.bingewatch.auth.models

data class GuestSession(
    var success: Boolean,
    val guest_session_id: String,
    val expires_at: String
)