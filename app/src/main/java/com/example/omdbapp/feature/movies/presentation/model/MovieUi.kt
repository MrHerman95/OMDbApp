package com.example.omdbapp.feature.movies.presentation.model

import com.example.omdbapp.R
import com.example.omdbapp.core.presentation.UiText
import com.example.omdbapp.feature.movies.domain.model.Movie
import com.example.omdbapp.feature.movies.domain.model.MovieType

data class MovieUi(
    val id: String,
    val title: String,
    val year: String,
    val typeLabel: UiText,
    val posterUrl: String?
)

fun Movie.toMovieUi(): MovieUi = MovieUi(
    id = id,
    title = title,
    year = year,
    typeLabel = type.toUiText(),
    posterUrl = posterUrl
)

fun MovieType.toUiText(): UiText {
    val id = when (this) {
        MovieType.MOVIE -> R.string.movie_type_movie
        MovieType.SERIES -> R.string.movie_type_series
        MovieType.EPISODE -> R.string.movie_type_episode
        MovieType.GAME -> R.string.movie_type_game
        MovieType.UNKNOWN -> R.string.movie_type_unknown
    }
    return UiText.StringResource(id)
}
