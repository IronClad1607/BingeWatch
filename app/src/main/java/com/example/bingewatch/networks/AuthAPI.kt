package com.example.bingewatch.networks

import com.example.bingewatch.R
import com.example.bingewatch.auth.models.GuestSessionResponse
import retrofit2.Response
import retrofit2.http.GET

interface AuthAPI {

    @GET("/3/authentication/guest_session/new?api_key=d5b568462e39f02e011bb612583ead1e")
    suspend fun getGuestSession() : Response<GuestSessionResponse>
}
