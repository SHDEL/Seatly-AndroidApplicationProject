package com.example.seatly

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.seatly.ui.screens.BookingSummaryScreen
import com.example.seatly.ui.screens.HomeScreen
import com.example.seatly.ui.screens.LoginScreen
import com.example.seatly.ui.screens.MovieDetailsScreen
import com.example.seatly.ui.screens.MovieViewModel
import com.example.seatly.ui.screens.ProfileScreen
import com.example.seatly.ui.screens.SearchScreen
import com.example.seatly.ui.screens.SeatDetailsScreen
import com.example.seatly.ui.screens.TicketsScreen
import com.example.seatly.ui.theme.SeatlyTheme

enum class AppDestinations(val route: String, val label: String, val icon: ImageVector) {
    HOME("home", "Home", Icons.Default.Home),
    SEARCH("search", "Search", Icons.Default.Search),
    TICKETS("tickets", "Tickets", Icons.Default.ConfirmationNumber),
    PROFILE("profile", "Profile", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeatlyTheme(darkTheme = true) {
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute != "login"

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Color(0xFF1A1A1A)) {
                    val currentAppDestination = AppDestinations.entries.find { it.route == currentRoute } ?: AppDestinations.HOME

                    AppDestinations.entries.forEach { destination ->
                        SeatlyNavigationBarItem(
                            destination = destination,
                            currentDestination = currentAppDestination,
                            onClick = { newDestination ->
                                if (newDestination.route != currentRoute) {
                                    navController.navigate(newDestination.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(onLoginClick = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }
            composable("home") {
                HomeScreen(
                    movieViewModel = movieViewModel,
                    retryAction = movieViewModel::getNowShowingWithGenres,
                    onMovieClick = { navController.navigate("details/${it.id}") }
                )
            }
            composable("search") {
                SearchScreen()
            }
            composable("tickets") {
                val tickets by movieViewModel.tickets
                TicketsScreen(tickets = tickets)
            }
            composable("profile") {
                ProfileScreen()
            }
            composable(
                route = "details/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId")
                val movie = movieViewModel.getMovieById(movieId)
                if (movie != null) {
                    MovieDetailsScreen(
                        movie = movie,
                        onBackClick = { navController.popBackStack() },
                        onNextClick = { id, date, time, hall -> 
                            navController.navigate("seats/$id/$date/$time/$hall") 
                        }
                    )
                }
            }
            composable(
                route = "seats/{movieId}/{date}/{time}/{hall}",
                arguments = listOf(
                    navArgument("movieId") { type = NavType.IntType },
                    navArgument("date") { type = NavType.StringType },
                    navArgument("time") { type = NavType.StringType },
                    navArgument("hall") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId")
                val date = backStackEntry.arguments?.getString("date")
                val time = backStackEntry.arguments?.getString("time")
                val hall = backStackEntry.arguments?.getString("hall")
                val movie = movieViewModel.getMovieById(movieId)
                if (movie != null && date != null && time != null && hall != null) {
                    SeatDetailsScreen(
                        movie = movie,
                        date = date,
                        time = time,
                        hall = hall,
                        onBackClick = { navController.popBackStack() },
                        onNextClick = { booking ->
                            movieViewModel.setBooking(booking)
                            navController.navigate("booking_summary/${movie.id}")
                        }
                    )
                }
            }
            composable(
                route = "booking_summary/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getInt("movieId")
                val movie = movieViewModel.getMovieById(movieId)
                val booking = movieViewModel.bookingState
                if (movie != null && booking != null) {
                    BookingSummaryScreen(
                        movie = movie,
                        booking = booking,
                        onBack = { navController.popBackStack() },
                        onConfirmBooking = {
                            movieViewModel.addTicket(movie, booking)
                            navController.navigate("tickets") {
                                popUpTo("home") // Clear back stack to home
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RowScope.SeatlyNavigationBarItem(
    destination: AppDestinations,
    currentDestination: AppDestinations,
    onClick: (AppDestinations) -> Unit
) {
    NavigationBarItem(
        icon = { Icon(destination.icon, contentDescription = destination.label) },
        label = { Text(destination.label) },
        selected = destination == currentDestination,
        onClick = { onClick(destination) },
        colors = NavigationBarItemDefaults.colors(
            unselectedIconColor = Color.Gray,
            unselectedTextColor = Color.Gray,
            selectedIconColor = Color(0xFFE50914),
            selectedTextColor = Color(0xFFE50914),
            indicatorColor = Color(0xFF2B2021)
        )
    )
}