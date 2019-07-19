package com.example.bingewatch.auth

data class GuestSessionResponse(
    var success: Boolean,
    val guest_session_id: String,
    val expires_at: String
)