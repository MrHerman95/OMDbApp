package com.example.omdbapp.core.designsystem.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omdbapp.core.designsystem.theme.OMDbAppTheme

@Composable
fun StatusMessage(
    message: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (actionLabel != null) {
            FilledTonalButton(onClick = onActionClick) {
                Text(text = actionLabel)
            }
        }
    }
}

@Preview(name = "Message only", showBackground = true)
@Preview(name = "Message only - Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun StatusMessagePreview() {
    OMDbAppTheme {
        Surface {
            StatusMessage(
                message = "Type a keyword to find movies",
                icon = Icons.Outlined.Movie,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(name = "With action", showBackground = true)
@Preview(name = "With action - Dark", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun StatusMessageWithActionPreview() {
    OMDbAppTheme {
        Surface {
            StatusMessage(
                message = "No internet connection. Check your network and try again.",
                icon = Icons.Outlined.CloudOff,
                actionLabel = "Retry",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(name = "Large font", showBackground = true, fontScale = 2f)
@Composable
private fun StatusMessageLargeFontPreview() {
    OMDbAppTheme {
        Surface {
            StatusMessage(
                message = "No internet connection. Check your network and try again.",
                icon = Icons.Outlined.CloudOff,
                actionLabel = "Retry",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
