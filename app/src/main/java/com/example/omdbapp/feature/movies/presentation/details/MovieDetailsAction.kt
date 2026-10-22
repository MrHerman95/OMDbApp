package com.example.omdbapp.feature.movies.presentation.details

sealed interface MovieDetailsAction {
    data object OnToggleFavoriteClick : MovieDetailsAction
    data object OnRetryClick : MovieDetailsAction
}
