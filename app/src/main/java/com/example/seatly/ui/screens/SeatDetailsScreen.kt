package com.example.seatly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seatly.model.Booking
import com.example.seatly.model.Movie
import com.example.seatly.model.Seat
import com.example.seatly.model.SeatStatus
import com.example.seatly.ui.theme.SeatlyTheme

// 3. สร้างข้อมูลจำลองของที่นั่ง
private fun generateDummySeats(): List<Seat> {
    val seats = mutableListOf<Seat>()
    val rows = 10
    val cols = 10
    for (i in 1..rows) {
        for (j in 1..cols) {
            val status = when {
                (i == 3 && j in 4..7) || (i == 5 && j == 5) -> SeatStatus.Reserved
                else -> SeatStatus.Available
            }
            seats.add(Seat(id = "R${i}N${j}", row = i, number = j, status = status))
        }
    }
    return seats
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatDetailsScreen(
    movie: Movie,
    date: String,
    time: String,
    hall: String,
    onBackClick: () -> Unit,
    onNextClick: (Booking) -> Unit
) {
    var seats by remember { mutableStateOf(generateDummySeats()) }
    val selectedSeats = seats.filter { it.status == SeatStatus.Selected }
    val pricePerSeat = 150.0
    val bookingFee = 10.0
    val taxes = 5.0
    val subtotal = selectedSeats.size * pricePerSeat
    val totalPrice = subtotal + bookingFee + taxes

    // Create a string of selected seat names
    val selectedSeatNames = selectedSeats.joinToString(", ") {
        val rowChar = ('A'.code + it.row - 1).toChar()
        "$rowChar${it.number}"
    }
    val selectedRow = if (selectedSeats.isNotEmpty()) {
        ('A'.code + selectedSeats.first().row - 1).toChar().toString()
    } else {
        ""
    }


    Scaffold(
        containerColor = Color.Black, // Match other screens
        topBar = {
            TopAppBar(
                title = { Text(movie.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors( // Themed TopAppBar
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(16.dp)
            ) {
                // Summary Text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${selectedSeats.size} Seats",
                            color = Color.White,
                            fontSize = 14.sp,
                        )
                        if (selectedSeatNames.isNotEmpty()) {
                            Text(
                                text = selectedSeatNames,
                                color = Color.LightGray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "Subtotal",
                            color = Color.LightGray,
                            fontSize = 14.sp,
                        )
                        Text(
                            text = "$totalPrice",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                // Themed Button
                Button(
                    onClick = { 
                        val booking = Booking(
                            hall = hall,
                            date = date,
                            time = time,
                            row = selectedRow,
                            seats = selectedSeatNames,
                            posterUrl = movie.poster,
                            priceItems = listOf(
                                "Tickets (${selectedSeats.size})" to subtotal,
                                "Booking Fee" to bookingFee,
                                "Taxes" to taxes
                            ),
                            total = totalPrice,
                            paymentMask = "Visa •••• 1234"
                        )
                        onNextClick(booking) 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
                ) {
                    Text("Next", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Screen representation
            Box(
                modifier = Modifier
                    .padding(bottom = 24.dp, start = 32.dp, end = 32.dp)
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(10),
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(seats) { seat ->
                    SeatItem(seat = seat) { clickedSeat ->
                        val updatedSeats = seats.map {
                            if (it.id == clickedSeat.id) {
                                when (it.status) {
                                    SeatStatus.Available -> it.copy(status = SeatStatus.Selected)
                                    SeatStatus.Selected -> it.copy(status = SeatStatus.Available)
                                    SeatStatus.Reserved -> it // Cannot change reserved seats
                                }
                            } else {
                                it
                            }
                        }
                        seats = updatedSeats
                    }
                }
            }
        }
    }
}

@Composable
fun SeatItem(seat: Seat, onSeatClick: (Seat) -> Unit) {
    val color = when (seat.status) {
        SeatStatus.Available -> Color(0xFF2D2D2D) // Themed available
        SeatStatus.Selected -> Color(0xFFE50914)  // Themed selected
        SeatStatus.Reserved -> Color.DarkGray     // Themed reserved
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .background(color, shape = RoundedCornerShape(8.dp))
            .clickable(enabled = seat.status != SeatStatus.Reserved) {
                onSeatClick(seat)
            },
        contentAlignment = Alignment.Center
    ) {
        if (seat.status == SeatStatus.Reserved) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Reserved",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        } else {
            // Display seat identifier like "A1", "B2"
            val rowChar = ('A'.code + seat.row - 1).toChar()
            Text(
                text = "$rowChar${seat.number}",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun SeatDetailsScreenPreview() {
    SeatlyTheme(darkTheme = true) {
        val mockMovie = Movie(
            id = 1,
            genre = listOf(28, 12),
            title = "Spider-Man: Across the Spider-Verse",
            overview = "Some overview",
            poster = "/8Vt6mWEReuy4Of61Lp5Sj7ShVvL.jpg",
            backdrop = "/5YEGzVj0V2E1g2sH3S4s39f4d1.jpg",
            releaseDate = "2023-05-31",
            voteAverage = 8.4,
            genreName = listOf("Action", "Adventure")
        )
        SeatDetailsScreen(movie = mockMovie, date = "Dec 23, 2024", time = "14:30", hall = "Hall 1", onBackClick = {}, onNextClick = {})
    }
}
