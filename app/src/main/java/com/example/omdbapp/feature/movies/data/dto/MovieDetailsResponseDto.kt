package com.example.omdbapp.feature.movies.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsResponseDto(
    @SerialName("imdbID") val imdbId: String? = null,
    @SerialName("Title") val title: String? = null,
    @SerialName("Year") val year: String? = null,
    @SerialName("Type") val type: String? = null,
    @SerialName("Poster") val poster: String? = null,
    @SerialName("Response") val response: String,
    @SerialName("Error") val error: String? = null
)
