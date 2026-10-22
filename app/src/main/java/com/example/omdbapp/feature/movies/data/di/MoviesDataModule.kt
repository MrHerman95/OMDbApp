package com.example.omdbapp.feature.movies.data.di

import com.example.omdbapp.feature.movies.data.repository.FavoriteMovieRepositoryImpl
import com.example.omdbapp.feature.movies.data.repository.MovieRepositoryImpl
import com.example.omdbapp.feature.movies.data.network.OmdbApi
import com.example.omdbapp.feature.movies.domain.repository.FavoriteMovieRepository
import com.example.omdbapp.feature.movies.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MoviesDataModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        repository: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteMovieRepository(
        repository: FavoriteMovieRepositoryImpl
    ): FavoriteMovieRepository

    companion object {
        @Provides
        @Singleton
        fun provideOmdbApi(retrofit: Retrofit): OmdbApi = retrofit.create(OmdbApi::class.java)
    }
}
