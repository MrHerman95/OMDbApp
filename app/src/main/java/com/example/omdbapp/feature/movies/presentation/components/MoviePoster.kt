package com.example.omdbapp.feature.movies.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.omdbapp.core.designsystem.theme.OMDbAppTheme

private const val POSTER_ASPECT_RATIO = 2f / 3f

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
internal fun MoviePoster(
    posterUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small
) {
    Box(
        modifier = modifier
            .aspectRatio(POSTER_ASPECT_RATIO)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Movie,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (posterUrl != null) {
            GlideImage(
                model = posterUrl,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(name = "No poster")
@Preview(name = "No poster - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MoviePosterPreview() {
    OMDbAppTheme {
        MoviePoster(
            posterUrl = null,
            contentDescription = null,
            modifier = Modifier.width(120.dp)
        )
    }
}
