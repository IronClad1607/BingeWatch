package com.example.bingewatch.model_movies

data class Reviews(
    val id:Int,
    val page:Int,
    val total_pages:Int,
    val total_results:Int,
    val results:ArrayList<Review>
)