package com.example.bingewatch.model_movies

data class MovieAllDetails(
    val backdrop_path: String,
    val budget: Int,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val genres: ArrayList<Genres>,
    val production_companies:ArrayList<ProductionCompanies>,
    val popularity: Number,
    val release_date: String,
    val runtime: Int,
    val tagline: String,
    val title: String,
    val vote_average: Double,
    val vote_count: Int
)


