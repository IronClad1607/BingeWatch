package com.example.bingewatch.networks

import com.example.bingewatch.model_movies.PopularMovies

suspend fun getPopularMovies(): PopularMovies? {
    val popularAPI = RetroClient.movieAPI
    val response = popularAPI.getPopularMovies()

    return if (response.isSuccessful) {
        response.body()
    } else {
        null
    }

}