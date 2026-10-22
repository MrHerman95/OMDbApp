package com.example.omdbapp.feature.movies.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.omdbapp.R
import com.example.omdbapp.core.designsystem.theme.OMDbAppTheme

@Composable
internal fun FavoriteButton(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val content: @Composable () -> Unit = {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = stringResource(
                if (isFavorite) R.string.details_remove_from_favorites
                else R.string.details_add_to_favorites
            )
        )
    }
    if (isFavorite) {
        FilledTonalButton(
            onClick = onFavoriteClick,
            modifier = modifier,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) { content() }
    } else {
        Button(
            onClick = onFavoriteClick,
            modifier = modifier,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) { content() }
    }
}

@Preview(name = "Not favorite")
@Preview(name = "Not favorite - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FavoriteButtonPreview() {
    OMDbAppTheme {
        Surface {
            FavoriteButton(isFavorite = false, onFavoriteClick = {})
        }
    }
}

@Preview(name = "Favorite")
@Preview(name = "Favorite - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FavoriteButtonFavoritePreview() {
    OMDbAppTheme {
        Surface {
            FavoriteButton(isFavorite = true, onFavoriteClick = {})
        }
    }
}
