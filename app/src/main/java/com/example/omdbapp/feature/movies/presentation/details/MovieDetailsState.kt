package com.example.omdbapp.feature.movies.presentation.details

import com.example.omdbapp.core.presentation.UiText
import com.example.omdbapp.feature.movies.presentation.model.MovieUi

data class MovieDetailsState(
    val movie: MovieUi? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = true,
    val error: UiText? = null
)
