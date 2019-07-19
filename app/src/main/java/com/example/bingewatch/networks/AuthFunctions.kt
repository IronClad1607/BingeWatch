package com.example.bingewatch.networks

import android.util.Log
import com.example.bingewatch.auth.*

suspend fun getGuestSession(): GuestSessionResponse? {

    val sessionAPI = RetroClient.authAPI

    val response = sessionAPI.getGuestSession()

    Log.d("Guest", "$response")

    return if (response.isSuccessful) {
        response.body()
    } else {
        null
    }
}

suspend fun getTokenRequest(): TokenResponse? {
    val tokenAPI = RetroClient.authAPI

    val response = tokenAPI.getLoginToken()

    return if (response.isSuccessful) {
        response.body()
    } else {
        null
    }
}

suspend fun postAuthTokenRequest(authRequestToken: AuthRequestToken): AuthResponse? {
    val authTokenAPI = RetroClient.authAPI

    val response = authTokenAPI.postAuthToken(authRequestToken)

    return if (response.isSuccessful) {
        response.body()
    } else {
        null
    }
}


suspend fun postSession(sessionResponseBody: SessionResponseBody): SessionResponse? {
    val sessionAPI = RetroClient.authAPI

    val response = sessionAPI.postSession(sessionResponseBody)

    return if (response.isSuccessful) {
        response.body()
    } else {
        null
    }
}