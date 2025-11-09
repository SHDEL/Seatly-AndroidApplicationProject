package com.example.seatly.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Theaters
//import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.seatly.R
import com.example.seatly.model.Movie
import com.example.seatly.ui.theme.SeatlyTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    movieViewModel: MovieViewModel,
    retryAction: () -> Unit,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val movieUiState = movieViewModel.movieUiState
    HomeScreenBody(
        movieUiState = movieUiState,
        onNowShowingClick = { movieViewModel.getNowShowingWithGenres() },
        onComingSoonClick = { movieViewModel.getComingWithGenres() },
        retryAction = retryAction,
        onMovieClick = onMovieClick,
        modifier = modifier,
        contentPadding = contentPadding
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenBody(
    movieUiState: MovieUiState,
    onNowShowingClick: () -> Unit,
    onComingSoonClick: () -> Unit,
    retryAction: () -> Unit,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var selectedTab by remember { mutableStateOf("Now Showing") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black) // Changed to pure black
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.Theaters,
            contentDescription = "App Icon",
            tint = Color(0xFFE50914),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .size(40.dp)
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.search)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF2D2D2D), shape = RoundedCornerShape(50))
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    selectedTab = "Now Showing"
                    onNowShowingClick()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                colors = if (selectedTab == "Now Showing") ButtonDefaults.buttonColors(containerColor = Color.DarkGray) else ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = stringResource(R.string.now_showing),
                    color = Color.White
                )
            }

            Button(
                onClick = {
                    selectedTab = "Coming Soon"
                    onComingSoonClick()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                colors = if (selectedTab == "Coming Soon") ButtonDefaults.buttonColors(containerColor = Color.DarkGray) else ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
            ) {
                Text(
                    stringResource(R.string.coming_soon),
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (movieUiState) {
            is MovieUiState.Loading -> LoadingScreen(modifier = Modifier.fillMaxSize())
            is MovieUiState.Success -> MovieList(
                movies = movieUiState.movies,
                onMovieClick = onMovieClick,
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding
            )
            is MovieUiState.Error -> ErrorScreen(retryAction, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun MovieList(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = contentPadding
    ) {
        items(movies) {
            val imgPath = "https://image.tmdb.org/t/p/w500/" + it.poster
            MovieListItem(movie = it, imgPath = imgPath, onMovieClick = onMovieClick)
        }
    }
}

@Composable
fun MovieListItem(movie: Movie, imgPath: String, onMovieClick: (Movie) -> Unit, modifier: Modifier = Modifier) {
    val rate = String.format("%.1f", movie.voteAverage)
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(imgPath)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = movie.title.ifEmpty { "Spider" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${rate} | ${movie.releaseDate}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = movie.genreName.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { onMovieClick(movie) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxSize()
                        .padding(12.dp)

                ) {
                    Text("Book Ticket",
                        color = Color.White)
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // ใช้ CircularProgressIndicator ให้หมุนอัตโนมัติ
            CircularProgressIndicator(
                color = Color(0xFFE50914),
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Error please try again")
        Button(onClick = retryAction) {
            Text("Retry")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun HomeScreenPreview() {
    SeatlyTheme(darkTheme = true) {
        val mockMovies = List(5) { movie ->
            Movie(
                id = movie,
                genre = listOf(28, 12),
                title = "Spider-Man: Across the Spider-Verse",
                overview = "",
                poster = "/8Vt6mWEReuy4Of61Lp5Sj7ShVvL.jpg",
                backdrop = "",
                releaseDate = "2023-05-31",
                voteAverage = 8.4,
                genreName = listOf("Action", "Adventure", "Sci-Fi")
            )
        }
        HomeScreenBody(
            movieUiState = MovieUiState.Success(mockMovies),
            onNowShowingClick = { },
            onComingSoonClick = { },
            retryAction = { },
            onMovieClick = { }
        )
    }
}
