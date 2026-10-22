package com.example.omdbapp.feature.movies.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MoviesGraphRoute

@Serializable
data object MovieSearchRoute

@Serializable
data object FavoritesRoute

@Serializable
data class MovieDetailsRoute(
    val movieId: String,
    val isFavorite: Boolean
)
