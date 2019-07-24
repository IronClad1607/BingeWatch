package com.example.bingewatch.model_movies

data class CreditsResponse(
    val id:Int,
    val cast:ArrayList<Cast>,
    val crew:ArrayList<Crew>
)