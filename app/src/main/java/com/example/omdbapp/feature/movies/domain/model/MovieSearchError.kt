package com.example.omdbapp.feature.movies.domain.model

import com.example.omdbapp.core.domain.util.DataError
import com.example.omdbapp.core.domain.util.Error

sealed interface MovieSearchError : Error {
    data class Network(val error: DataError.Network) : MovieSearchError

    data object TooManyResults : MovieSearchError
}
