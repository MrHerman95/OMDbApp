package com.example.omdbapp.feature.movies.presentation.favorites

import com.example.omdbapp.feature.movies.presentation.model.MovieUi

data class FavoritesState(
    val favorites: List<MovieUi> = emptyList(),
    val isLoading: Boolean = true
)
