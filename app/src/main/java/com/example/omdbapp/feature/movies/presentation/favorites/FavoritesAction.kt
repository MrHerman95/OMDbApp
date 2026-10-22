package com.example.omdbapp.feature.movies.presentation.favorites

sealed interface FavoritesAction {
    data class OnRemoveFavoriteClick(val movieId: String) : FavoritesAction
}
