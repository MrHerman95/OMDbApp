package com.example.omdbapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.omdbapp.core.database.dao.FavoriteMovieDao
import com.example.omdbapp.core.database.entity.FavoriteMovieEntity

@Database(
    entities = [FavoriteMovieEntity::class],
    version = 1
)
abstract class OmdbDatabase : RoomDatabase() {
    abstract val favoriteMovieDao: FavoriteMovieDao

    companion object {
        const val NAME = "omdb.db"
    }
}
