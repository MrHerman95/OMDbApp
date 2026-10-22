package com.example.omdbapp.feature.movies.data.repository

import com.example.omdbapp.core.data.networking.safeCall
import com.example.omdbapp.core.domain.util.DataError
import com.example.omdbapp.core.domain.util.Result
import com.example.omdbapp.feature.movies.data.mappers.toMovie
import com.example.omdbapp.feature.movies.data.mappers.toMovieOrNull
import com.example.omdbapp.feature.movies.data.network.OmdbApi
import com.example.omdbapp.feature.movies.domain.model.Movie
import com.example.omdbapp.feature.movies.domain.repository.MovieRepository
import com.example.omdbapp.feature.movies.domain.model.MovieSearchError
import com.example.omdbapp.feature.movies.domain.model.MovieSearchPage
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: OmdbApi
) : MovieRepository {

    override suspend fun searchMovies(
        query: String,
        page: Int
    ): Result<MovieSearchPage, MovieSearchError> {
        val result = safeCall { api.searchMovies(query = query, page = page) }
        val response = when (result) {
            is Result.Error -> return Result.Error(MovieSearchError.Network(result.error))
            is Result.Success -> result.data
        }

        if (response.response.isOmdbSuccess()) {
            return Result.Success(
                MovieSearchPage(
                    movies = response.search.map { it.toMovie() },
                    totalResults = response.totalResults?.toIntOrNull() ?: 0
                )
            )
        }

        return when (response.error) {
            OMDB_ERROR_MOVIE_NOT_FOUND -> Result.Success(
                MovieSearchPage(movies = emptyList(), totalResults = 0)
            )
            OMDB_ERROR_TOO_MANY_RESULTS -> Result.Error(MovieSearchError.TooManyResults)
            else -> Result.Error(MovieSearchError.Network(DataError.Network.UNKNOWN))
        }
    }

    override suspend fun getMovie(id: String): Result<Movie, DataError.Network> {
        val result = safeCall { api.getMovie(imdbId = id) }
        val response = when (result) {
            is Result.Error -> return result
            is Result.Success -> result.data
        }

        if (!response.response.isOmdbSuccess()) {
            return Result.Error(DataError.Network.NOT_FOUND)
        }
        val movie = response.toMovieOrNull()
            ?: return Result.Error(DataError.Network.SERIALIZATION)
        return Result.Success(movie)
    }

    private fun String.isOmdbSuccess(): Boolean = equals("True", ignoreCase = true)

    private companion object {
        const val OMDB_ERROR_MOVIE_NOT_FOUND = "Movie not found!"
        const val OMDB_ERROR_TOO_MANY_RESULTS = "Too many results."
    }
}
