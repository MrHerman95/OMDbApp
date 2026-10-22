package com.example.omdbapp.feature.movies.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.omdbapp.feature.movies.presentation.details.MovieDetailsRoot
import com.example.omdbapp.feature.movies.presentation.favorites.FavoritesRoot
import com.example.omdbapp.feature.movies.presentation.search.MovieSearchRoot

fun NavGraphBuilder.moviesGraph(navController: NavController) {
    navigation<MoviesGraphRoute>(startDestination = MovieSearchRoute) {
        composable<MovieSearchRoute> {
            MovieSearchRoot(
                onMovieClick = { movieId, isFavorite ->
                    navController.navigate(MovieDetailsRoute(movieId = movieId, isFavorite = isFavorite))
                },
                onFavoritesClick = {
                    navController.navigate(FavoritesRoute) { launchSingleTop = true }
                }
            )
        }
        composable<FavoritesRoute> {
            FavoritesRoot(
                onMovieClick = { movieId ->
                    navController.navigate(MovieDetailsRoute(movieId = movieId, isFavorite = true))
                },
                onBackClick = navController::navigateUp
            )
        }

        composable<MovieDetailsRoute> {
            MovieDetailsRoot(
                onBackClick = navController::navigateUp
            )
        }
    }
}
