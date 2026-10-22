package com.example.omdbapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.omdbapp.feature.movies.presentation.navigation.MoviesGraphRoute
import com.example.omdbapp.feature.movies.presentation.navigation.moviesGraph

@Composable
fun OmdbNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MoviesGraphRoute,
        modifier = modifier
    ) {
        moviesGraph(navController = navController)
    }
}
