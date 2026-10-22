package com.example.omdbapp.feature.movies.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omdbapp.core.domain.util.onFailure
import com.example.omdbapp.core.presentation.toUiText
import com.example.omdbapp.feature.movies.domain.repository.FavoriteMovieRepository
import com.example.omdbapp.feature.movies.presentation.model.toMovieUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state = _state.asStateFlow()

    private val _events = Channel<FavoritesEvent>()
    val events = _events.receiveAsFlow()

    init {
        observeFavorites()
    }

    fun onAction(action: FavoritesAction) {
        when (action) {
            is FavoritesAction.OnRemoveFavoriteClick -> removeFavorite(action.movieId)
        }
    }

    private fun observeFavorites() {
        favoriteMovieRepository.observeFavorites()
            .onEach { movies ->
                _state.update {
                    it.copy(favorites = movies.map { movie -> movie.toMovieUi() }, isLoading = false)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun removeFavorite(movieId: String) {
        viewModelScope.launch {
            favoriteMovieRepository.removeFavorite(movieId)
                .onFailure { error ->
                    _events.send(FavoritesEvent.ShowError(error.toUiText()))
                }
        }
    }
}
