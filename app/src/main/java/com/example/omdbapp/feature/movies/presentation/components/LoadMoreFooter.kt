package com.example.omdbapp.feature.movies.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omdbapp.R
import com.example.omdbapp.core.designsystem.theme.OMDbAppTheme

@Composable
internal fun LoadMoreFooter(
    isLoading: Boolean,
    errorMessage: String?,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when {
            isLoading -> CircularProgressIndicator()
            errorMessage != null -> {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = onRetryClick) {
                    Text(text = stringResource(R.string.action_retry))
                }
            }
        }
    }
}

@Preview(name = "Loading")
@Preview(name = "Loading - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LoadMoreFooterLoadingPreview() {
    OMDbAppTheme {
        Surface {
            LoadMoreFooter(isLoading = true, errorMessage = null, onRetryClick = {})
        }
    }
}

@Preview(name = "Error")
@Preview(name = "Error - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun LoadMoreFooterErrorPreview() {
    OMDbAppTheme {
        Surface {
            LoadMoreFooter(
                isLoading = false,
                errorMessage = "Couldn't load more results",
                onRetryClick = {}
            )
        }
    }
}
