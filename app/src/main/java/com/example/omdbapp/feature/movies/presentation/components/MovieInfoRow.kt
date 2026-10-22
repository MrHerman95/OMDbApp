package com.example.omdbapp.feature.movies.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omdbapp.core.designsystem.theme.OMDbAppTheme

@Composable
internal fun MovieInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(name = "Default")
@Preview(name = "Default - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MovieInfoRowPreview() {
    OMDbAppTheme {
        Surface {
            MovieInfoRow(label = "Type", value = "Movie", modifier = Modifier.padding(16.dp))
        }
    }
}

@Preview(name = "Large font", fontScale = 2f, widthDp = 320)
@Composable
private fun MovieInfoRowLargeFontPreview() {
    OMDbAppTheme {
        Surface {
            MovieInfoRow(label = "Year", value = "2019–2024", modifier = Modifier.padding(16.dp))
        }
    }
}
