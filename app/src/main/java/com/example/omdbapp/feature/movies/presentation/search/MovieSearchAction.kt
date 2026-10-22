package com.example.omdbapp.feature.movies.presentation.search

sealed interface MovieSearchAction {
    data class OnQueryChange(val query: String) : MovieSearchAction
    data object OnSearchClick : MovieSearchAction
    data object OnClearQueryClick : MovieSearchAction
    data object OnRetryClick : MovieSearchAction
    data object OnLoadNextPage : MovieSearchAction
    data object OnRetryNextPageClick : MovieSearchAction
    data class OnToggleFavoriteClick(val movieId: String) : MovieSearchAction
}
