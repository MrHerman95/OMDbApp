package com.example.omdbapp.feature.movies.domain.model

data class MovieSearchPage(
    val movies: List<Movie>,
    val totalResults: Int
)
