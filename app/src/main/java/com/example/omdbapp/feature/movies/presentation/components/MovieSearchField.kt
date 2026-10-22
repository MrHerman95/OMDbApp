package com.example.omdbapp.feature.movies.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omdbapp.R
import com.example.omdbapp.core.designsystem.theme.OMDbAppTheme

@Composable
internal fun MovieSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text(text = stringResource(R.string.search_hint)) },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearClick) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = stringResource(R.string.cd_clear_search)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                onSearch()
            }
        )
    )
}

@Preview(name = "Empty")
@Preview(name = "Empty - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MovieSearchFieldEmptyPreview() {
    OMDbAppTheme {
        Surface {
            MovieSearchField(
                query = "",
                onQueryChange = {},
                onSearch = {},
                onClearClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "With query")
@Preview(name = "With query - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MovieSearchFieldQueryPreview() {
    OMDbAppTheme {
        Surface {
            MovieSearchField(
                query = "love",
                onQueryChange = {},
                onSearch = {},
                onClearClick = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
