package com.example.seatly.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seatly.ui.screens.HomeScreen
import com.example.seatly.ui.screens.MovieViewModel


//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun MovieApp(modifier: Modifier){
//    Scaffold(modifier) {
//        Surface(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            val movieViewModel: MovieViewModel = viewModel(factory = MovieViewModel.Factory)
////            HomeScreen(
////                movieViewModel = movieViewModel,
////                retryAction = movieViewModel::getNowShowingWithGenres,
////                contentPadding = it
////            )
//        }
//
//    }
//}

