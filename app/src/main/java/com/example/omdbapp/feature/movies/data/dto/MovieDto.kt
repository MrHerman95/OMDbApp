package com.example.omdbapp.feature.movies.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    @SerialName("imdbID") val imdbId: String,
    @SerialName("Title") val title: String,
    @SerialName("Year") val year: String,
    @SerialName("Type") val type: String,
    @SerialName("Poster") val poster: String? = null
)
