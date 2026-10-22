package com.example.omdbapp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.omdbapp.core.database.entity.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movies ORDER BY addedAtMillis DESC")
    fun observeAll(): Flow<List<FavoriteMovieEntity>>

    @Query("SELECT imdbId FROM favorite_movies")
    fun observeIds(): Flow<List<String>>

    @Query("SELECT * FROM favorite_movies WHERE imdbId = :imdbId")
    suspend fun getById(imdbId: String): FavoriteMovieEntity?

    @Upsert
    suspend fun upsert(movie: FavoriteMovieEntity)

    @Query("DELETE FROM favorite_movies WHERE imdbId = :imdbId")
    suspend fun deleteById(imdbId: String)
}
