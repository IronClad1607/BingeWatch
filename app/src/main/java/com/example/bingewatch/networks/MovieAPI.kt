package com.example.bingewatch.networks

import com.example.bingewatch.model_movies.CreditsResponse
import com.example.bingewatch.model_movies.MovieAllDetails
import com.example.bingewatch.model_movies.PopularMovies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {

    @GET("/3/movie/popular?api_key=d5b568462e39f02e011bb612583ead1e&language=en-US")
    suspend fun getPopularMovies(@Query("page") page: Int): Response<PopularMovies>

    @GET("/3/movie/now_playing?api_key=d5b568462e39f02e011bb612583ead1e&language=en-US")
    suspend fun getInCinema(@Query("page") page: Int): Response<PopularMovies>

    @GET("/3/movie/upcoming?api_key=d5b568462e39f02e011bb612583ead1e&language=en-US")
    suspend fun getUpcoming(@Query("page") page: Int): Response<PopularMovies>

    @GET("/3/movie/top_rated?api_key=d5b568462e39f02e011bb612583ead1e&language=en-US")
    suspend fun getTopRated(@Query("page") page: Int): Response<PopularMovies>

    @GET("/3/movie/{movie_id}?api_key=d5b568462e39f02e011bb612583ead1e&language=en-US")
    suspend fun getMoviesDetails(@Path("movie_id") movie_id: Int): Response<MovieAllDetails>

    @GET("/3/movie/{movie_id}/credits?api_key=d5b568462e39f02e011bb612583ead1e")
    suspend fun getCreditsDetails(@Path("movie_id") movie_id: Int):Response<CreditsResponse>
}