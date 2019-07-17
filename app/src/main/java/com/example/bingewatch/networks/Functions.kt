package com.example.bingewatch.networks

import android.util.Log
import com.example.bingewatch.auth.models.GuestSessionResponse

suspend fun getGuestSession(): GuestSessionResponse? {

    val sessionAPI = RetroClient.authAPI

    val response = sessionAPI.getGuestSession()

    Log.d("Guest","$response")

    return if (response.isSuccessful) {
        response.body()
    } else {
        null
    }
}