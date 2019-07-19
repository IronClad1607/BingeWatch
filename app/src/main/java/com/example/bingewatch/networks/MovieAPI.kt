package com.example.bingewatch.networks

import com.example.bingewatch.model_movies.PopularMovies
import retrofit2.Response
import retrofit2.http.GET

interface MovieAPI {

    @GET("/3/movie/popular?api_key=d5b568462e39f02e011bb612583ead1e&language=en-US&page=1")
    suspend fun getPopularMovies() :Response<PopularMovies>
}