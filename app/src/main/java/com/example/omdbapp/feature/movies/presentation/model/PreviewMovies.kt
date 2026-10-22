package com.example.omdbapp.feature.movies.presentation.model

import com.example.omdbapp.R
import com.example.omdbapp.core.presentation.UiText

internal val previewMovies = listOf(
    MovieUi(
        id = "tt0314331",
        title = "Love Actually",
        year = "2003",
        typeLabel = UiText.StringResource(R.string.movie_type_movie),
        posterUrl = null
    ),
    MovieUi(
        id = "tt1570728",
        title = "Crazy, Stupid, Love.",
        year = "2011",
        typeLabel = UiText.StringResource(R.string.movie_type_movie),
        posterUrl = null
    ),
    MovieUi(
        id = "tt8484160",
        title = "Love, Death & Robots: A Long Title That Has To Wrap Onto Multiple Lines",
        year = "2019–",
        typeLabel = UiText.StringResource(R.string.movie_type_series),
        posterUrl = null
    )
)
