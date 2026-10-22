package com.example.omdbapp.feature.movies.presentation.model

import com.example.omdbapp.R
import com.example.omdbapp.core.presentation.UiText
import com.example.omdbapp.core.presentation.toUiText
import com.example.omdbapp.feature.movies.domain.model.MovieSearchError

fun MovieSearchError.toUiText(): UiText {
    return when (this) {
        is MovieSearchError.Network -> error.toUiText()
        MovieSearchError.TooManyResults -> UiText.StringResource(R.string.error_too_many_results)
    }
}
