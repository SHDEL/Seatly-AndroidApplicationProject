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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.seatly.model.Cinema
import com.example.seatly.model.Movie
import com.example.seatly.ui.theme.SeatlyTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

val mockCinemas = listOf(
    Cinema("Hall 1", listOf("10:00", "13:00", "16:00", "19:00", "22:00")),
    Cinema("Hall 2", listOf("11:30", "14:30", "17:30", "20:30")),
    Cinema("Hall 3", listOf("12:00", "15:00", "18:00", "21:00"))
)
// Mock data for dates
@RequiresApi(Build.VERSION_CODES.O)
fun getNext7Days(): List<Pair<String, String>> {
    val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val dayFormat = SimpleDateFormat("dd", Locale.getDefault())
    val calendar = Calendar.getInstance()
    return (0..6).map {
        calendar.add(Calendar.DAY_OF_YEAR, if (it == 0) 0 else 1)
        Pair(dateFormat.format(calendar.time), dayFormat.format(calendar.time))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MovieDetailsScreen(movie: Movie, onBackClick: () -> Unit, onNextClick: (movieId: Int, date: String, time: String, hall: String) -> Unit) {
    val rate = String.format("%.1f", movie.voteAverage)
    var selectedDateIndex by remember { mutableStateOf(0) }
    val dates = getNext7Days()
    var cinemaMenuExpanded by remember { mutableStateOf(false) }
    var selectedCinema by remember { mutableStateOf(mockCinemas.first()) }
    var selectedTime by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Showtime") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Backdrop Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
            {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://image.tmdb.org/t/p/w780/${movie.backdrop}")
                        .crossfade(true)
                        .build(),
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(alpha = 0.6f)
                    ))
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("https://image.tmdb.org/t/p/w500/${movie.poster}")
                            .crossfade(true)
                            .build(),
                        contentDescription = movie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(120.dp)
                            .height(180.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.headlineSmall,
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
                                text = "$rate | ${movie.releaseDate}",
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
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Cinema Selector
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    "Select Hall",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(12.dp))
                ExposedDropdownMenuBox(
                    expanded = cinemaMenuExpanded,
                    onExpandedChange = { cinemaMenuExpanded = !cinemaMenuExpanded }
                ) {
                    TextField(
                        value = selectedCinema.name,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cinemaMenuExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color(0xFF2D2D2D),
                            unfocusedContainerColor = Color(0xFF2D2D2D),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = cinemaMenuExpanded,
                        onDismissRequest = { cinemaMenuExpanded = false }
                    ) {
                        mockCinemas.forEach { cinema ->
                            DropdownMenuItem(
                                text = { Text(cinema.name) },
                                onClick = {
                                    selectedCinema = cinema
                                    cinemaMenuExpanded = false
                                    selectedTime = null // Reset time selection when cinema changes
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))


            // Date Selector
            Text(
                "Select Date",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(dates.size) {
                    val (day, date) = dates[it]
                    DateChip(
                        day = day,
                        date = date,
                        isSelected = selectedDateIndex == it,
                        onClick = { selectedDateIndex = it }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Cinema and Showtimes
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text("Available Showtime", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(selectedCinema.showtime) { time ->
                        val isSelected = selectedTime == time
                        Button(
                            onClick = { selectedTime = time },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Color(0xFFE50914) else Color(0xFF2D2D2D)
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(time, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            // Select Seats Button
            Button(
                onClick = { 
                    val (day, date) = dates[selectedDateIndex]
                    val formattedDate = "$day, $date"
                    selectedTime?.let { time ->
                        onNextClick(movie.id, formattedDate, time, selectedCinema.name)
                    } 
                },
                enabled = selectedTime != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
            ) {
                Text("Next", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun DateChip(day: String, date: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) Color(0xFFE50914) else Color(0xFF2D2D2D)
    val contentColor = if (isSelected) Color.White else Color.LightGray
    Card(
        modifier = Modifier.width(70.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(day, color = contentColor, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(date, color = contentColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun MovieDetailsScreenPreview() {
    SeatlyTheme(darkTheme = true) {
        val mockMovie = Movie(
            id = 1,
            genre = listOf(28, 12, 16, 878),
            title = "Spider-Man: Across the Spider-Verse",
            overview = "After reuniting with Gwen Stacy, Brooklyn’s full-time, friendly neighborhood Spider-Man is catapulted across the Multiverse, where he encounters a team of Spider-People charged with protecting its very existence. But when the heroes clash on how to handle a new threat, Miles finds himself pitted against the other Spiders and must redefine what it means to be a hero.",
            poster = "/8Vt6mWEReuy4Of61Lp5Sj7ShVvL.jpg",
            backdrop = "/5YEGzVj0V2E1g2sH3S4s39f4d1.jpg",
            releaseDate = "2023-05-31",
            voteAverage = 8.4,
            genreName = listOf("Action", "Adventure", "Animation", "Science Fiction")
        )
        MovieDetailsScreen(movie = mockMovie, onBackClick = {}, onNextClick = {_, _, _, _ -> })
    }
}
