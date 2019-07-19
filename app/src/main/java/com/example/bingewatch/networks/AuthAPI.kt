package com.example.bingewatch.networks

import com.example.bingewatch.auth.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthAPI {

    @GET("/3/authentication/guest_session/new?api_key=d5b568462e39f02e011bb612583ead1e")
    suspend fun getGuestSession() : Response<GuestSessionResponse>

    @GET("/3/authentication/token/new?api_key=d5b568462e39f02e011bb612583ead1e")
    suspend fun getLoginToken(): Response<TokenResponse>


    @POST("/3/authentication/token/validate_with_login?api_key=d5b568462e39f02e011bb612583ead1e")
    suspend fun postAuthToken(@Body authRequestToken: AuthRequestToken) : Response<AuthResponse>


    @POST("/3/authentication/session/new?api_key=d5b568462e39f02e011bb612583ead1e")
    suspend fun postSession(@Body sessionResponseBody: SessionResponseBody) : Response<SessionResponse>
}

