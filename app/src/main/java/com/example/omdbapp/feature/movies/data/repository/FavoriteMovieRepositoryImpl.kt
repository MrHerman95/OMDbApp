package com.example.omdbapp.feature.movies.data.repository

import android.database.sqlite.SQLiteFullException
import com.example.omdbapp.core.database.dao.FavoriteMovieDao
import com.example.omdbapp.core.domain.util.DataError
import com.example.omdbapp.core.domain.util.EmptyResult
import com.example.omdbapp.core.domain.util.Result
import com.example.omdbapp.feature.movies.data.mappers.toFavoriteMovieEntity
import com.example.omdbapp.feature.movies.data.mappers.toMovie
import com.example.omdbapp.feature.movies.domain.repository.FavoriteMovieRepository
import com.example.omdbapp.feature.movies.domain.model.Movie
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteMovieRepositoryImpl @Inject constructor(
    private val dao: FavoriteMovieDao
) : FavoriteMovieRepository {

    override fun observeFavorites(): Flow<List<Movie>> {
        return dao.observeAll().map { entities -> entities.map { it.toMovie() } }
    }

    override fun observeFavoriteIds(): Flow<Set<String>> {
        return dao.observeIds().map { it.toSet() }
    }

    override suspend fun getFavorite(id: String): Result<Movie, DataError.Local> {
        return localCall {
            dao.getById(id)?.toMovie()
                ?: return Result.Error(DataError.Local.NOT_FOUND)
        }
    }

    override suspend fun addFavorite(movie: Movie): EmptyResult<DataError.Local> {
        return localCall {
            dao.upsert(movie.toFavoriteMovieEntity(addedAtMillis = System.currentTimeMillis()))
        }
    }

    override suspend fun removeFavorite(id: String): EmptyResult<DataError.Local> {
        return localCall { dao.deleteById(id) }
    }

    private inline fun <T> localCall(execute: () -> T): Result<T, DataError.Local> {
        return try {
            Result.Success(execute())
        } catch (e: SQLiteFullException) {
            e.printStackTrace()
            Result.Error(DataError.Local.DISK_FULL)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            Result.Error(DataError.Local.UNKNOWN)
        }
    }
}
