package com.example.omdbapp.feature.movies.presentation.search

import com.example.omdbapp.core.presentation.UiText

sealed interface MovieSearchEvent {
    data class ShowError(val message: UiText) : MovieSearchEvent
}
