package com.example.omdbapp.feature.movies.presentation.details

import com.example.omdbapp.core.presentation.UiText

sealed interface MovieDetailsEvent {
    data class ShowError(val message: UiText) : MovieDetailsEvent
}
