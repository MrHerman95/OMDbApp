package com.example.omdbapp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_movies")
data class FavoriteMovieEntity(
    @PrimaryKey val imdbId: String,
    val title: String,
    val year: String,
    val type: String,
    val posterUrl: String?,
    val addedAtMillis: Long
)
