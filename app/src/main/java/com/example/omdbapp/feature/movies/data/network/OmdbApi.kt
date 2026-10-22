package com.example.omdbapp.feature.movies.data.network

import com.example.omdbapp.feature.movies.data.dto.MovieDetailsResponseDto
import com.example.omdbapp.feature.movies.data.dto.SearchResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApi {

    @GET("/")
    suspend fun searchMovies(
        @Query("s") query: String,
        @Query("page") page: Int
    ): Response<SearchResponseDto>

    @GET("/")
    suspend fun getMovie(
        @Query("i") imdbId: String
    ): Response<MovieDetailsResponseDto>
}
