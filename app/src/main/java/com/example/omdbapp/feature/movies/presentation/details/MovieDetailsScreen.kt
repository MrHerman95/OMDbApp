package com.example.omdbapp.feature.movies.presentation.details

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.omdbapp.R
import com.example.omdbapp.core.designsystem.components.StatusMessage
import com.example.omdbapp.core.designsystem.theme.OMDbAppTheme
import com.example.omdbapp.core.presentation.ObserveAsEvents
import com.example.omdbapp.core.presentation.UiText
import com.example.omdbapp.feature.movies.presentation.components.FavoriteButton
import com.example.omdbapp.feature.movies.presentation.components.MovieInfoRow
import com.example.omdbapp.feature.movies.presentation.components.MoviePoster
import com.example.omdbapp.feature.movies.presentation.model.MovieUi
import com.example.omdbapp.feature.movies.presentation.model.previewMovies
import kotlinx.coroutines.launch

@Composable
fun MovieDetailsRoot(
    onBackClick: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MovieDetailsEvent.ShowError -> scope.launch {
                snackbarHostState.showSnackbar(event.message.asString(context))
            }
        }
    }

    MovieDetailsScreen(
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    state: MovieDetailsState,
    onAction: (MovieDetailsAction) -> Unit,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> StatusMessage(
                    message = state.error.asString(),
                    icon = Icons.Outlined.CloudOff,
                    actionLabel = stringResource(R.string.action_retry),
                    onActionClick = { onAction(MovieDetailsAction.OnRetryClick) }
                )
                state.movie != null -> MovieDetailsContent(
                    movie = state.movie,
                    isFavorite = state.isFavorite,
                    onFavoriteClick = { onAction(MovieDetailsAction.OnToggleFavoriteClick) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun MovieDetailsContent(
    movie: MovieUi,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MoviePoster(
            posterUrl = movie.posterUrl,
            contentDescription = stringResource(R.string.cd_poster, movie.title),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth(0.6f)
        )
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier.widthIn(max = 480.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MovieInfoRow(
                label = stringResource(R.string.details_type_label),
                value = movie.typeLabel.asString()
            )
            if (movie.year.isNotBlank()) {
                MovieInfoRow(
                    label = stringResource(R.string.details_year_label),
                    value = movie.year
                )
            }
        }
        FavoriteButton(
            isFavorite = isFavorite,
            onFavoriteClick = onFavoriteClick
        )
    }
}

@Preview(name = "Loaded")
@Preview(name = "Loaded - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MovieDetailsScreenPreview() {
    OMDbAppTheme {
        MovieDetailsScreen(
            state = MovieDetailsState(movie = previewMovies[0], isLoading = false),
            onAction = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Favorite, long title")
@Composable
private fun MovieDetailsScreenFavoritePreview() {
    OMDbAppTheme {
        MovieDetailsScreen(
            state = MovieDetailsState(movie = previewMovies[2], isFavorite = true, isLoading = false),
            onAction = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Landscape", widthDp = 840, heightDp = 400)
@Composable
private fun MovieDetailsScreenWidePreview() {
    OMDbAppTheme {
        MovieDetailsScreen(
            state = MovieDetailsState(movie = previewMovies[0], isLoading = false),
            onAction = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Loading")
@Composable
private fun MovieDetailsScreenLoadingPreview() {
    OMDbAppTheme {
        MovieDetailsScreen(
            state = MovieDetailsState(isLoading = true),
            onAction = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Error")
@Composable
private fun MovieDetailsScreenErrorPreview() {
    OMDbAppTheme {
        MovieDetailsScreen(
            state = MovieDetailsState(
                isLoading = false,
                error = UiText.StringResource(R.string.error_no_internet)
            ),
            onAction = {},
            onBackClick = {}
        )
    }
}
