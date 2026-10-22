package com.example.omdbapp.feature.movies.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    @SerialName("Search") val search: List<MovieDto> = emptyList(),
    @SerialName("totalResults") val totalResults: String? = null,
    @SerialName("Response") val response: String,
    @SerialName("Error") val error: String? = null
)
