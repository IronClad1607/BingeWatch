package com.example.bingewatch.model_movies

data class PopularMovies(
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: ArrayList<Result>
)

data class Result(
    val release_date:String,

    val id:Int,
    val poster_path: String?,
    val original_title: String
)


