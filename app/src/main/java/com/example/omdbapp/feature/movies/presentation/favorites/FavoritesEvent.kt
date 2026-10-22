package com.example.omdbapp.feature.movies.presentation.favorites

import com.example.omdbapp.core.presentation.UiText

sealed interface FavoritesEvent {
    data class ShowError(val message: UiText) : FavoritesEvent
}
