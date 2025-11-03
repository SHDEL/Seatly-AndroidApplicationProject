package com.example.seatly.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seatly.ui.screens.HomeScreen
import com.example.seatly.ui.screens.MovieViewModel


@Composable
fun MovieApp(modifier: Modifier){
    Scaffold(modifier) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val movieViewModel: MovieViewModel = viewModel(factory = MovieViewModel.Factory)
            HomeScreen(
                movieUiState = movieViewModel.movieUiState,
                retryAction = movieViewModel::getMovieNowShow,
                contentPadding = it
            )
        }

    }
}

