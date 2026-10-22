package com.example.omdbapp.feature.movies.domain.model

data class Movie(
    val id: String,
    val title: String,
    val year: String,
    val type: MovieType,
    val posterUrl: String?
)
