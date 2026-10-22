package com.example.omdbapp.feature.movies.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omdbapp.core.designsystem.theme.OMDbAppTheme
import com.example.omdbapp.feature.movies.presentation.model.previewMovies

@Composable
internal fun MovieListItem(
    title: String,
    year: String,
    typeLabel: String,
    posterUrl: String?,
    isFavorite: Boolean,
    onMovieClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onMovieClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MoviePoster(
                posterUrl = posterUrl,
                // The title next to the poster already describes it
                contentDescription = null,
                modifier = Modifier.width(64.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "$year · $typeLabel",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            FavoriteIconButton(
                movieTitle = title,
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Preview(name = "Default")
@Preview(name = "Default - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MovieListItemPreview() {
    OMDbAppTheme {
        MovieListItem(
            title = previewMovies[0].title,
            year = previewMovies[0].year,
            typeLabel = "Movie",
            posterUrl = null,
            isFavorite = false,
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "Favorite")
@Composable
private fun MovieListItemFavoritePreview() {
    OMDbAppTheme {
        MovieListItem(
            title = previewMovies[0].title,
            year = previewMovies[0].year,
            typeLabel = "Movie",
            posterUrl = null,
            isFavorite = true,
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "Long title", widthDp = 320)
@Composable
private fun MovieListItemLongTitlePreview() {
    OMDbAppTheme {
        MovieListItem(
            title = previewMovies[2].title,
            year = previewMovies[2].year,
            typeLabel = "Series",
            posterUrl = null,
            isFavorite = false,
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "Wide", widthDp = 840)
@Composable
private fun MovieListItemWidePreview() {
    OMDbAppTheme {
        MovieListItem(
            title = previewMovies[1].title,
            year = previewMovies[1].year,
            typeLabel = "Movie",
            posterUrl = null,
            isFavorite = false,
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "Large font", fontScale = 2f)
@Composable
private fun MovieListItemLargeFontPreview() {
    OMDbAppTheme {
        MovieListItem(
            title = previewMovies[1].title,
            year = previewMovies[1].year,
            typeLabel = "Movie",
            posterUrl = null,
            isFavorite = true,
            onMovieClick = {},
            onFavoriteClick = {}
        )
    }
}
