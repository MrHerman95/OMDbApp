package com.example.omdbapp.feature.movies.presentation.search

import com.example.omdbapp.core.presentation.UiText
import com.example.omdbapp.feature.movies.presentation.model.MovieUi

data class MovieSearchState(
    val query: String = "",
    val searchedQuery: String = "",
    val movies: List<MovieUi> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val isLoadingNextPage: Boolean = false,
    val isEndReached: Boolean = false,
    val error: UiText? = null,
    val nextPageError: UiText? = null
)
