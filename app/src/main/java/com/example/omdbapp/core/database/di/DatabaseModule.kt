package com.example.omdbapp.core.database.di

import android.content.Context
import androidx.room.Room
import com.example.omdbapp.core.database.OmdbDatabase
import com.example.omdbapp.core.database.dao.FavoriteMovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): OmdbDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = OmdbDatabase::class.java,
            name = OmdbDatabase.NAME
        ).build()
    }

    @Provides
    fun provideFavoriteMovieDao(database: OmdbDatabase): FavoriteMovieDao {
        return database.favoriteMovieDao
    }
}
