package com.example.omdbapp.feature.movies.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.omdbapp.core.domain.util.DataError
import com.example.omdbapp.core.domain.util.Result
import com.example.omdbapp.core.domain.util.onFailure
import com.example.omdbapp.core.domain.util.onSuccess
import com.example.omdbapp.core.presentation.toUiText
import com.example.omdbapp.feature.movies.domain.repository.FavoriteMovieRepository
import com.example.omdbapp.feature.movies.domain.model.Movie
import com.example.omdbapp.feature.movies.domain.repository.MovieRepository
import com.example.omdbapp.feature.movies.presentation.model.toMovieUi
import com.example.omdbapp.feature.movies.presentation.navigation.MovieDetailsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
    private val favoriteMovieRepository: FavoriteMovieRepository
) : ViewModel() {

    private val route = savedStateHandle.toRoute<MovieDetailsRoute>()
    private val movieId = route.movieId

    private val _state = MutableStateFlow(MovieDetailsState())
    val state = _state.asStateFlow()

    private val _events = Channel<MovieDetailsEvent>()
    val events = _events.receiveAsFlow()

    private var movie: Movie? = null

    init {
        observeIsFavorite()
        loadMovie()
    }

    fun onAction(action: MovieDetailsAction) {
        when (action) {
            MovieDetailsAction.OnToggleFavoriteClick -> toggleFavorite()
            MovieDetailsAction.OnRetryClick -> loadMovie()
        }
    }

    private fun observeIsFavorite() {
        favoriteMovieRepository.observeFavoriteIds()
            .map { ids -> movieId in ids }
            .onEach { isFavorite -> _state.update { it.copy(isFavorite = isFavorite) } }
            .launchIn(viewModelScope)
    }

    private fun loadMovie() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result: Result<Movie, DataError> = if (route.isFavorite) {
                favoriteMovieRepository.getFavorite(movieId)
            } else {
                movieRepository.getMovie(movieId)
            }
            result
                .onSuccess { loadedMovie ->
                    movie = loadedMovie
                    _state.update {
                        it.copy(movie = loadedMovie.toMovieUi(), isLoading = false)
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                }
        }
    }

    private fun toggleFavorite() {
        val currentMovie = movie ?: return
        val isFavorite = _state.value.isFavorite
        viewModelScope.launch {
            val result = if (isFavorite) {
                favoriteMovieRepository.removeFavorite(currentMovie.id)
            } else {
                favoriteMovieRepository.addFavorite(currentMovie)
            }
            result.onFailure { error ->
                _events.send(MovieDetailsEvent.ShowError(error.toUiText()))
            }
        }
    }
}
