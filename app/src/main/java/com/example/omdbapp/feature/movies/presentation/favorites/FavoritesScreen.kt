package com.example.omdbapp.feature.movies.presentation.favorites

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.omdbapp.R
import com.example.omdbapp.core.designsystem.components.StatusMessage
import com.example.omdbapp.core.designsystem.theme.OMDbAppTheme
import com.example.omdbapp.core.presentation.ObserveAsEvents
import com.example.omdbapp.feature.movies.presentation.components.MovieListItem
import com.example.omdbapp.feature.movies.presentation.model.previewMovies
import kotlinx.coroutines.launch

@Composable
fun FavoritesRoot(
    onMovieClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is FavoritesEvent.ShowError -> scope.launch {
                snackbarHostState.showSnackbar(event.message.asString(context))
            }
        }
    }

    FavoritesScreen(
        state = state,
        onAction = viewModel::onAction,
        onMovieClick = onMovieClick,
        onBackClick = onBackClick,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    state: FavoritesState,
    onAction: (FavoritesAction) -> Unit,
    onMovieClick: (String) -> Unit,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.favorites_title)) },
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
                state.favorites.isEmpty() -> StatusMessage(
                    message = stringResource(R.string.favorites_empty),
                    icon = Icons.Outlined.FavoriteBorder
                )
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = state.favorites, key = { it.id }) { movie ->
                        MovieListItem(
                            title = movie.title,
                            year = movie.year,
                            typeLabel = movie.typeLabel.asString(),
                            posterUrl = movie.posterUrl,
                            isFavorite = true,
                            onMovieClick = { onMovieClick(movie.id) },
                            onFavoriteClick = {
                                onAction(FavoritesAction.OnRemoveFavoriteClick(movie.id))
                            },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Favorites")
@Preview(name = "Favorites - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FavoritesScreenPreview() {
    OMDbAppTheme {
        FavoritesScreen(
            state = FavoritesState(favorites = previewMovies, isLoading = false),
            onAction = {},
            onMovieClick = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Empty")
@Preview(name = "Empty - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun FavoritesScreenEmptyPreview() {
    OMDbAppTheme {
        FavoritesScreen(
            state = FavoritesState(isLoading = false),
            onAction = {},
            onMovieClick = {},
            onBackClick = {}
        )
    }
}
