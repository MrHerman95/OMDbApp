package com.example.omdbapp.feature.movies.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.omdbapp.R
import com.example.omdbapp.core.designsystem.theme.OMDbAppTheme

@Composable
internal fun FavoriteIconButton(
    movieTitle: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = { onFavoriteClick() },
        modifier = modifier,
        colors = IconButtonDefaults.iconToggleButtonColors(
            checkedContentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = stringResource(
                if (isFavorite) R.string.cd_remove_from_favorites else R.string.cd_add_to_favorites,
                movieTitle
            )
        )
    }
}

@Preview(name = "Not favorite")
@Preview(name = "Not favorite - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FavoriteIconButtonPreview() {
    OMDbAppTheme {
        Surface {
            FavoriteIconButton(movieTitle = "Love Actually", isFavorite = false, onFavoriteClick = {})
        }
    }
}

@Preview(name = "Favorite")
@Preview(name = "Favorite - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FavoriteIconButtonCheckedPreview() {
    OMDbAppTheme {
        Surface {
            FavoriteIconButton(movieTitle = "Love Actually", isFavorite = true, onFavoriteClick = {})
        }
    }
}
