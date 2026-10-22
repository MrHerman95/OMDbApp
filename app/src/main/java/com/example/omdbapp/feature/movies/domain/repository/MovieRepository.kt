package com.example.omdbapp.feature.movies.domain.repository

import com.example.omdbapp.core.domain.util.DataError
import com.example.omdbapp.core.domain.util.Result
import com.example.omdbapp.feature.movies.domain.model.Movie
import com.example.omdbapp.feature.movies.domain.model.MovieSearchError
import com.example.omdbapp.feature.movies.domain.model.MovieSearchPage

interface MovieRepository {
    suspend fun searchMovies(query: String, page: Int): Result<MovieSearchPage, MovieSearchError>
    suspend fun getMovie(id: String): Result<Movie, DataError.Network>
}
