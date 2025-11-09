package com.example.seatly

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.seatly.ui.screens.HomeScreen
import com.example.seatly.ui.screens.MovieDetailsScreen
import com.example.seatly.ui.screens.MovieViewModel
import com.example.seatly.ui.screens.SeatDetailsScreen
import com.example.seatly.ui.theme.SeatlyTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeatlyTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SeatlyApp()
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SeatlyApp() {
    val navController = rememberNavController()
    val movieViewModel: MovieViewModel = viewModel(factory = MovieViewModel.Factory)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == "home" } == true,
                    onClick = { navController.navigate("home") { popUpTo(navController.graph.findStartDestination().id) { saveState = true }; launchSingleTop = true; restoreState = true } },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                )
                NavigationBarItem(
                    selected = false, // TODO: Update when search screen is available
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    label = { Text("Search") }
                )
                NavigationBarItem(
                    selected = false, // TODO: Update when tickets screen is available
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = "Tickets") },
                    label = { Text("Tickets") }
                )
                NavigationBarItem(
                    selected = false, // TODO: Update when profile screen is available
                    onClick = { /* TODO */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    movieViewModel = movieViewModel,
                    retryAction = movieViewModel::getNowShowingWithGenres,
                    onMovieClick = {
                        // Navigate to details screen
                        navController.navigate("details/${it.id}")
                    }
                )
            }
            composable(
                route = "details/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) {
                backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId")
                val movie = movieViewModel.getMovieById(movieId)
                if (movie != null) {
                    MovieDetailsScreen(
                        movie = movie,
                        onBackClick = { navController.popBackStack() },
                        onNextClick = {
                            navController.navigate("seats/${movie.id}")
                        }
                    )
                }
            }
            composable(
                route = "seats/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ){
                    backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId")
                val movie = movieViewModel.getMovieById(movieId)
                if (movie != null) {
                    SeatDetailsScreen(
                        movie = movie,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}