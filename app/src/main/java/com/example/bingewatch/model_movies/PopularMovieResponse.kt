package com.example.bingewatch.model_movies

data class PopularMovies(
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: ArrayList<MoviesDetails>
)



