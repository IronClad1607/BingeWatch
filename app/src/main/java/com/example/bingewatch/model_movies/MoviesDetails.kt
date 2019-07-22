package com.example.bingewatch.model_movies

data class MoviesDetails(
    val release_date: String,
    val backdrop_path: String,
    val id: Int,
    val genres: Array<Int>,
    val poster_path: String?,
    val title: String,
    val overview: String,
    val popularity: Double,
    val vote_count: Int,
    val vote_average: Double,
    val original_title: String
)