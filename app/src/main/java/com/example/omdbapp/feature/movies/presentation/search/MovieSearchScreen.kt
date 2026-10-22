package com.example.omdbapp.feature.movies.presentation.search

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Card
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.omdbapp.core.presentation.UiText
import com.example.omdbapp.feature.movies.presentation.components.LoadMoreFooter
import com.example.omdbapp.feature.movies.presentation.components.MovieListItem
import com.example.omdbapp.feature.movies.presentation.components.MovieSearchField
import com.example.omdbapp.feature.movies.presentation.model.previewMovies
import com.example.sdk.ads.nativead.AdListItem
import com.example.sdk.ads.nativead.NativeAdPlacement
import com.example.sdk.ads.nativead.NativeAdSlot
import kotlinx.coroutines.launch

private const val LOAD_NEXT_PAGE_THRESHOLD = 3

@Composable
fun MovieSearchRoot(
    onMovieClick: (movieId: String, isFavorite: Boolean) -> Unit,
    onFavoritesClick: () -> Unit,
    viewModel: MovieSearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MovieSearchEvent.ShowError -> scope.launch {
                snackbarHostState.showSnackbar(event.message.asString(context))
            }
        }
    }

    MovieSearchScreen(
        state = state,
        onAction = viewModel::onAction,
        onMovieClick = onMovieClick,
        onFavoritesClick = onFavoritesClick,
        snackbarHostState = snackbarHostState,
        adPlacement = viewModel.nativeAds.placement,
        nativeAdContent = { slot, modifier ->
            NativeAdSlot(controller = viewModel.nativeAds, slot = slot, modifier = modifier)
        }
    )
}

/**
 * @param adPlacement where native ads go between the results.
 * @param nativeAdContent renders the ad of a slot; a slot without an ad renders nothing.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchScreen(
    state: MovieSearchState,
    onAction: (MovieSearchAction) -> Unit,
    onMovieClick: (movieId: String, isFavorite: Boolean) -> Unit,
    onFavoritesClick: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    adPlacement: NativeAdPlacement = NativeAdPlacement.None,
    nativeAdContent: @Composable (slot: Int, modifier: Modifier) -> Unit = { _, _ -> }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.search_title)) },
                actions = {
                    IconButton(onClick = onFavoritesClick) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = stringResource(R.string.cd_open_favorites)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
        ) {
            MovieSearchField(
                query = state.query,
                onQueryChange = { onAction(MovieSearchAction.OnQueryChange(it)) },
                onSearch = { onAction(MovieSearchAction.OnSearchClick) },
                onClearClick = { onAction(MovieSearchAction.OnClearQueryClick) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    state.isLoading -> CircularProgressIndicator()
                    state.error != null -> StatusMessage(
                        message = state.error.asString(),
                        icon = Icons.Outlined.CloudOff,
                        actionLabel = stringResource(R.string.action_retry),
                        onActionClick = { onAction(MovieSearchAction.OnRetryClick) }
                    )
                    state.searchedQuery.isBlank() -> StatusMessage(
                        message = stringResource(R.string.search_initial_message),
                        icon = Icons.Outlined.Movie
                    )
                    state.movies.isEmpty() -> StatusMessage(
                        message = stringResource(R.string.search_no_results, state.searchedQuery),
                        icon = Icons.Outlined.SearchOff
                    )
                    else -> MovieSearchResults(
                        state = state,
                        onAction = onAction,
                        onMovieClick = onMovieClick,
                        adPlacement = adPlacement,
                        nativeAdContent = nativeAdContent
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieSearchResults(
    state: MovieSearchState,
    onAction: (MovieSearchAction) -> Unit,
    onMovieClick: (movieId: String, isFavorite: Boolean) -> Unit,
    adPlacement: NativeAdPlacement,
    nativeAdContent: @Composable (slot: Int, modifier: Modifier) -> Unit
) {
    val listState = rememberSaveable(state.searchedQuery, saver = LazyListState.Saver) {
        LazyListState()
    }
    val listItems = remember(state.movies, adPlacement) {
        adPlacement.insertInto(state.movies)
    }
    val isNearEnd by remember(listState) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= layoutInfo.totalItemsCount - 1 - LOAD_NEXT_PAGE_THRESHOLD
        }
    }
    LaunchedEffect(isNearEnd, state.movies.size) {
        if (isNearEnd) {
            onAction(MovieSearchAction.OnLoadNextPage)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = listItems,
            key = { item ->
                when (item) {
                    is AdListItem.Content -> item.value.id
                    is AdListItem.Ad -> "native_ad_${item.slot}"
                }
            },
            contentType = { item ->
                when (item) {
                    is AdListItem.Content -> "movie"
                    is AdListItem.Ad -> "native_ad"
                }
            }
        ) { item ->
            when (item) {
                is AdListItem.Content -> {
                    val movie = item.value
                    MovieListItem(
                        title = movie.title,
                        year = movie.year,
                        typeLabel = movie.typeLabel.asString(),
                        posterUrl = movie.posterUrl,
                        isFavorite = movie.id in state.favoriteIds,
                        onMovieClick = { onMovieClick(movie.id, movie.id in state.favoriteIds) },
                        onFavoriteClick = {
                            onAction(MovieSearchAction.OnToggleFavoriteClick(movie.id))
                        },
                        modifier = Modifier.animateItem()
                    )
                }
                is AdListItem.Ad -> nativeAdContent(item.slot, Modifier.animateItem())
            }
        }
        if (state.isLoadingNextPage || state.nextPageError != null) {
            item(key = "load_more_footer") {
                LoadMoreFooter(
                    isLoading = state.isLoadingNextPage,
                    errorMessage = state.nextPageError?.asString(),
                    onRetryClick = { onAction(MovieSearchAction.OnRetryNextPageClick) }
                )
            }
        }
    }
}

@Preview(name = "Initial")
@Preview(name = "Initial - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MovieSearchScreenInitialPreview() {
    OMDbAppTheme {
        MovieSearchScreen(
            state = MovieSearchState(),
            onAction = {},
            onMovieClick = { _, _ -> },
            onFavoritesClick = {}
        )
    }
}

@Preview(name = "Loading")
@Composable
private fun MovieSearchScreenLoadingPreview() {
    OMDbAppTheme {
        MovieSearchScreen(
            state = MovieSearchState(query = "love", searchedQuery = "love", isLoading = true),
            onAction = {},
            onMovieClick = { _, _ -> },
            onFavoritesClick = {}
        )
    }
}

@Preview(name = "Results")
@Preview(name = "Results - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MovieSearchScreenResultsPreview() {
    OMDbAppTheme {
        MovieSearchScreen(
            state = MovieSearchState(
                query = "love",
                searchedQuery = "love",
                movies = previewMovies,
                favoriteIds = setOf(previewMovies[1].id),
                isLoadingNextPage = true
            ),
            onAction = {},
            onMovieClick = { _, _ -> },
            onFavoritesClick = {}
        )
    }
}

@Preview(name = "Results with ads")
@Preview(name = "Results with ads - Dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MovieSearchScreenWithAdsPreview() {
    OMDbAppTheme {
        MovieSearchScreen(
            state = MovieSearchState(
                query = "love",
                searchedQuery = "love",
                movies = previewMovies
            ),
            onAction = {},
            onMovieClick = { _, _ -> },
            onFavoritesClick = {},
            adPlacement = NativeAdPlacement(positions = listOf(1, 3), minContentCount = 3),
            nativeAdContent = { slot, modifier ->
                // Real ads need a live ad network, so previews show a stand-in
                Card(modifier = modifier.fillMaxWidth()) {
                    Text(text = "Native ad, slot $slot", modifier = Modifier.padding(16.dp))
                }
            }
        )
    }
}

@Preview(name = "No results")
@Composable
private fun MovieSearchScreenEmptyPreview() {
    OMDbAppTheme {
        MovieSearchScreen(
            state = MovieSearchState(query = "zzqxqx", searchedQuery = "zzqxqx"),
            onAction = {},
            onMovieClick = { _, _ -> },
            onFavoritesClick = {}
        )
    }
}

@Preview(name = "Error")
@Composable
private fun MovieSearchScreenErrorPreview() {
    OMDbAppTheme {
        MovieSearchScreen(
            state = MovieSearchState(
                query = "love",
                searchedQuery = "love",
                error = UiText.StringResource(R.string.error_no_internet)
            ),
            onAction = {},
            onMovieClick = { _, _ -> },
            onFavoritesClick = {}
        )
    }
}
