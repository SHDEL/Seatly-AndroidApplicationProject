package com.example.seatly.ui.screens

import androidx.annotation.Px
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.seatly.SeatlyApp
import com.example.seatly.model.Movie
import com.example.seatly.ui.theme.SeatlyTheme

@Composable
fun HomeScreen(
    movieUiState: MovieUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
){
    when (movieUiState) {
        is MovieUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is MovieUiState.Success -> PhotosGridScreen(
            movieUiState.movies, contentPadding = contentPadding, modifier = modifier.fillMaxWidth()
        )
        is MovieUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }

}
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Text("Loading...")
}

/**
 * The home screen displaying error message with re-attempt button.
 */
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

/**
 * The home screen displaying photo grid.
 */
@Composable
fun PhotosGridScreen(
    movies: List<Movie>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {

    Column( Modifier.fillMaxWidth()) {

        Row(Modifier.weight(1f)) {
            Box {
                Text(
                    text = "Now Showing",
                    fontWeight =  FontWeight.Bold,
                    modifier = modifier.padding(10.dp)
                )
                LazyRow(
                    modifier = modifier.padding(horizontal = 4.dp).padding(top = 10.dp),
                    contentPadding = contentPadding,
                ) {
                    items(movies) { movie ->
                        val imgPath = "https://image.tmdb.org/t/p/w500/" + movie.poster
                        MovieCard(
                            movie,
                            imgPath,
                            modifier
                        )
                    }
                }
            }

        }
        Row(Modifier.weight(1f)) {

        }

    }
}

@Composable
fun MovieCard(movie: Movie, imgPath: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(4.dp).fillMaxWidth().aspectRatio(0.66f),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column() {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(imgPath)
                    .crossfade(true)
                    .build(),
                contentDescription = "MoviePhoto",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }





}

@Preview(showBackground = true)
@Composable
fun PhotoGridPreview() {
    SeatlyTheme {
        val mockdata = List(5) { Movie(1, "", "", "", "", "", 0.0)}
        PhotosGridScreen(mockdata)
    }
}

