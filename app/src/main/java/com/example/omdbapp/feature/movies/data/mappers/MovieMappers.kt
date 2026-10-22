package com.example.omdbapp.feature.movies.data.mappers

import com.example.omdbapp.core.database.entity.FavoriteMovieEntity
import com.example.omdbapp.feature.movies.data.dto.MovieDetailsResponseDto
import com.example.omdbapp.feature.movies.data.dto.MovieDto
import com.example.omdbapp.feature.movies.domain.model.Movie
import com.example.omdbapp.feature.movies.domain.model.MovieType

private const val OMDB_NOT_AVAILABLE = "N/A"

fun MovieDto.toMovie(): Movie = Movie(
    id = imdbId,
    title = title,
    year = year,
    type = type.toMovieType(),
    posterUrl = poster.toPosterUrl()
)

fun MovieDetailsResponseDto.toMovieOrNull(): Movie? {
    return Movie(
        id = imdbId ?: return null,
        title = title ?: return null,
        year = year.orEmpty(),
        type = type.orEmpty().toMovieType(),
        posterUrl = poster.toPosterUrl()
    )
}

fun FavoriteMovieEntity.toMovie(): Movie = Movie(
    id = imdbId,
    title = title,
    year = year,
    type = MovieType.entries.find { it.name == type } ?: MovieType.UNKNOWN,
    posterUrl = posterUrl
)

fun Movie.toFavoriteMovieEntity(addedAtMillis: Long): FavoriteMovieEntity = FavoriteMovieEntity(
    imdbId = id,
    title = title,
    year = year,
    type = type.name,
    posterUrl = posterUrl,
    addedAtMillis = addedAtMillis
)

private fun String.toMovieType(): MovieType {
    return when (lowercase()) {
        "movie" -> MovieType.MOVIE
        "series" -> MovieType.SERIES
        "episode" -> MovieType.EPISODE
        "game" -> MovieType.GAME
        else -> MovieType.UNKNOWN
    }
}

private fun String?.toPosterUrl(): String? {
    return this?.takeIf { it.isNotBlank() && it != OMDB_NOT_AVAILABLE }
}
