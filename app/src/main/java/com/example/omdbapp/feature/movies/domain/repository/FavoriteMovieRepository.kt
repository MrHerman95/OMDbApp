package com.example.omdbapp.feature.movies.domain.repository

import com.example.omdbapp.core.domain.util.DataError
import com.example.omdbapp.core.domain.util.EmptyResult
import com.example.omdbapp.core.domain.util.Result
import com.example.omdbapp.feature.movies.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface FavoriteMovieRepository {
    fun observeFavorites(): Flow<List<Movie>>
    fun observeFavoriteIds(): Flow<Set<String>>
    suspend fun getFavorite(id: String): Result<Movie, DataError.Local>
    suspend fun addFavorite(movie: Movie): EmptyResult<DataError.Local>
    suspend fun removeFavorite(id: String): EmptyResult<DataError.Local>
}
