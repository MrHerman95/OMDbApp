package com.example.omdbapp.feature.movies.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdbapp.core.domain.util.onFailure
import com.example.omdbapp.core.domain.util.onSuccess
import com.example.omdbapp.core.presentation.toUiText
import com.example.omdbapp.feature.movies.domain.repository.FavoriteMovieRepository
import com.example.omdbapp.feature.movies.domain.model.Movie
import com.example.omdbapp.feature.movies.domain.repository.MovieRepository
import com.example.omdbapp.feature.movies.presentation.model.toMovieUi
import com.example.omdbapp.feature.movies.presentation.model.toUiText
import com.example.sdk.ads.AdsSdk
import com.example.sdk.ads.nativead.NativeAdPlacement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieSearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
    private val favoriteMovieRepository: FavoriteMovieRepository,
    adsSdk: AdsSdk
) : ViewModel() {

    val nativeAds = adsSdk.createNativeAdsController(
        placement = SEARCH_RESULTS_AD_PLACEMENT,
        scope = viewModelScope
    )

    private val _state = MutableStateFlow(
        MovieSearchState(query = savedStateHandle[QUERY_KEY] ?: "")
    )
    val state = _state.asStateFlow()

    private val _events = Channel<MovieSearchEvent>()
    val events = _events.receiveAsFlow()

    private val queryFlow = MutableStateFlow(_state.value.query)
    private var searchJob: Job? = null

    private var searchResults: List<Movie> = emptyList()
    private var loadedPage = 0
    private var fetchedCount = 0

    init {
        observeFavoriteIds()
        observeQuery()
    }

    fun onAction(action: MovieSearchAction) {
        when (action) {
            is MovieSearchAction.OnQueryChange -> onQueryChange(action.query)
            MovieSearchAction.OnSearchClick -> search(_state.value.query.trim())
            MovieSearchAction.OnClearQueryClick -> {
                onQueryChange("")
                search("")
            }
            MovieSearchAction.OnRetryClick -> search(_state.value.searchedQuery)
            MovieSearchAction.OnLoadNextPage -> loadNextPage()
            MovieSearchAction.OnRetryNextPageClick -> {
                _state.update { it.copy(nextPageError = null) }
                loadNextPage()
            }
            is MovieSearchAction.OnToggleFavoriteClick -> toggleFavorite(action.movieId)
        }
    }

    private fun onQueryChange(query: String) {
        savedStateHandle[QUERY_KEY] = query
        _state.update { it.copy(query = query) }
        queryFlow.value = query
    }

    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        queryFlow
            .debounce(SEARCH_DEBOUNCE_MILLIS)
            .map { it.trim() }
            .distinctUntilChanged()
            .onEach { query ->
                if (query != _state.value.searchedQuery) {
                    search(query)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeFavoriteIds() {
        favoriteMovieRepository.observeFavoriteIds()
            .onEach { ids -> _state.update { it.copy(favoriteIds = ids) } }
            .launchIn(viewModelScope)
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchResults = emptyList()
        loadedPage = 0
        fetchedCount = 0

        _state.update {
            it.copy(
                searchedQuery = query,
                movies = emptyList(),
                isLoading = query.isNotBlank(),
                isLoadingNextPage = false,
                isEndReached = false,
                error = null,
                nextPageError = null
            )
        }
        if (query.isBlank()) return

        searchJob = viewModelScope.launch {
            loadPage(query = query, page = 1)
        }
    }

    private fun loadNextPage() {
        val currentState = _state.value
        val canLoadNextPage = currentState.searchedQuery.isNotBlank() &&
            !currentState.isLoading &&
            !currentState.isLoadingNextPage &&
            !currentState.isEndReached &&
            currentState.error == null &&
            currentState.nextPageError == null
        if (!canLoadNextPage) return

        _state.update { it.copy(isLoadingNextPage = true) }
        searchJob = viewModelScope.launch {
            loadPage(query = currentState.searchedQuery, page = loadedPage + 1)
        }
    }

    private suspend fun loadPage(query: String, page: Int) {
        movieRepository.searchMovies(query = query, page = page)
            .onSuccess { result ->
                loadedPage = page
                fetchedCount += result.movies.size
                searchResults = (searchResults + result.movies).distinctBy { it.id }
                _state.update {
                    it.copy(
                        movies = searchResults.map { movie -> movie.toMovieUi() },
                        isLoading = false,
                        isLoadingNextPage = false,
                        isEndReached = result.movies.isEmpty() || fetchedCount >= result.totalResults
                    )
                }
            }
            .onFailure { error ->
                _state.update {
                    if (page == 1) {
                        it.copy(isLoading = false, error = error.toUiText())
                    } else {
                        it.copy(isLoadingNextPage = false, nextPageError = error.toUiText())
                    }
                }
            }
    }

    private fun toggleFavorite(movieId: String) {
        val movie = searchResults.find { it.id == movieId } ?: return
        val isFavorite = movieId in _state.value.favoriteIds
        viewModelScope.launch {
            val result = if (isFavorite) {
                favoriteMovieRepository.removeFavorite(movieId)
            } else {
                favoriteMovieRepository.addFavorite(movie)
            }
            result.onFailure { error ->
                _events.send(MovieSearchEvent.ShowError(error.toUiText()))
            }
        }
    }

    override fun onCleared() {
        nativeAds.destroy()
    }

    private companion object {
        const val QUERY_KEY = "query"
        const val SEARCH_DEBOUNCE_MILLIS = 500L

        val SEARCH_RESULTS_AD_PLACEMENT = NativeAdPlacement(
            positions = listOf(1, 3),
            minContentCount = 3
        )
    }
}
