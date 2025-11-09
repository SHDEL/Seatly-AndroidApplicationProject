package com.example.seatly.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.seatly.data.Booking
import com.example.seatly.data.MovieRepository // Keep this for mockup data
import com.example.seatly.data.Ticket
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Theaters
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.seatly.model.Movie
import com.example.seatly.Component.HomeScreenBody

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    HOME("Home", Icons.Default.Home),
    SEARCH("Search", Icons.Filled.Search),
    TICKET("Ticket", Icons.Filled.ConfirmationNumber),
    PROFILE("Profile", Icons.Filled.Person),
}

@Composable
fun TicketsScreen(
    tickets: List<Ticket>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onTicketClick: (Ticket) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Active, 1 = Past

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Set background color for the whole screen
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                contentDescription = "Back",
//                tint = Color.White,
//                modifier = Modifier
//                    .size(28.dp)
//                    .align(Alignment.CenterStart)
//                    .clickable { onBack() }
//            )

            Text(
                text = "My Tickets",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(Modifier.height(16.dp))

        // This is the TabRow from your image
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Surface(
                shape = RoundedCornerShape(24.dp),color = Color(0xFF2A2A2A),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Row(
                    // --- แก้ไข Modifier ของ Row นี้ ---
                    modifier = Modifier
                        .fillMaxSize() // 1. ให้ Row ขยายเต็มพื้นที่ Surface
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly, // 2. จัดให้ปุ่มกระจายตัวเท่าๆ กัน
                    verticalAlignment = Alignment.CenterVertically // 3. จัดให้อยู่กึ่งกลางแนวตั้ง
                ) {
                    TabButton(text = "Active", selected = selectedTab == 0) { selectedTab = 0 }
                    TabButton(text = "Past", selected = selectedTab == 1) { selectedTab = 1 }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // simple filtering example: you can replace with real date-check
        val shown = remember(tickets, selectedTab) {
            if (selectedTab == 0) tickets else tickets.reversed() // placeholder behavior
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(shown) { ticket ->
                TicketCard(ticket = ticket, onClick = { onTicketClick(ticket) })
            }
        }
    }
}

@Composable
private fun RowScope.TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (selected) Color(0xFFE50914) else Color.Transparent
    val contentColor = if (selected) Color.White else Color.LightGray

    Box(
        modifier = Modifier
            .weight(1f) // <-- 2. แบ่งพื้นที่เท่าๆ กัน (คนละครึ่ง)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 6.dp), // เอา padding แนวนอนออกไป
        contentAlignment = Alignment.Center // 3. จัดให้ Text อยู่ตรงกลาง Box
    ) {
        Text(
            text = text,
            color = contentColor,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 14.sp
        )
    }
}

@Composable
fun HomeScreenBody(
    movies: List<Movie>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
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
            label = { Text("Search") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(30.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        var selectedTab by remember { mutableStateOf("Now Showing") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF2D2D2D), shape = RoundedCornerShape(50))
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { selectedTab = "Now Showing" },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                colors = if (selectedTab == "Now Showing") ButtonDefaults.buttonColors(containerColor = Color.DarkGray) else ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) { Text("Now Showing",
                color = Color.White) }

            Button(
                onClick = { selectedTab = "Coming Soon" },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50),
                colors = if (selectedTab == "Coming Soon") ButtonDefaults.buttonColors(containerColor = Color.DarkGray) else ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            ) { Text("Coming Soon") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = contentPadding
        ) {
            items(movies) {
                val imgPath = "https://image.tmdb.org/t/p/w500/" + it.poster
                MovieListItem(movie = it, imgPath = imgPath)
            }
        }
    }
}

@Composable
fun TicketCard(ticket: Ticket, onClick: () -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Give card a fixed height
            .clickable { onClick() },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Movie Poster as background
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${ticket.movie.poster}",
                contentDescription = ticket.movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Black overlay for better text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            // Content on top of the image
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Bottom // Align content to the bottom
            ) {
                // Movie Title and Date (Top Part inside the bottom section)
                Text(
                    text = ticket.movie.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${ticket.booking.date} | ${ticket.booking.time}",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(12.dp))

                // Divider Line
                Divider(color = Color.Gray.copy(alpha = 0.5f), thickness = 1.dp)

                Spacer(Modifier.height(12.dp))

                // Number of tickets and Seat Number (Bottom Part)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Number of Tickets
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EventSeat,
                            contentDescription = "Tickets",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        @SuppressLint("ModifierSize")
                        Text(
                            // แก้ไขบรรทัดนี้: ให้นับจำนวนที่นั่งจากการ split string
                            text = "${ticket.booking.seats.split(',').size} Tickets",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Seat Info
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EventSeat,
                            contentDescription = "Seats",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            // แก้ไขบรรทัดนี้: ลบ ?.joinToString(...) ออกไปเลย
                            text = ticket.booking.seats,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TicketsScreenPreview() {

    // --- ส่วนที่ 1: สร้างข้อมูล Mockup สำหรับ Preview ---
    val movie1 = com.example.seatly.model.Movie(
        id = 1,
        title = "Spider-Man: Across the Spider-Verse",
        overview = "Miles Morales catapults across the Multiverse...",
        poster = "/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg",
        backdrop = "",
        releaseDate = "2023-05-31",
        voteAverage = 8.4
    )

    val movie2 = com.example.seatly.model.Movie(
        id = 2,
        title = "Dune: Part Two",
        overview = "Follow the mythic journey of Paul Atreides...",
        poster = "/1m02V5NF2l2b1TfLkrrydrw1hEu.jpg",
        backdrop = "",
        releaseDate = "2024-02-27",
        voteAverage = 8.3
    )

    val sampleTickets = listOf(
        Ticket(
            movie = movie1,
            booking = Booking(
                hall = "Cinema 5",
                date = "SUN, MAR 9, 2025",
                time = "14:30",
                row = "B",
                seats = "B2, B3"
            )
        ),
        Ticket(
            movie = movie2,
            booking = Booking(
                hall = "IMAX Hall 1",
                date = "MON, MAR 10, 2025",
                time = "19:00",
                row = "E",
                seats = "E10, E11, E12"
            )
        )
    )

    // --- ส่วนที่ 2: สร้างโครงสร้าง UI สำหรับ Preview ---
    // ใช้ Theme ของแอปเพื่อให้สีสันและฟอนต์ถูกต้อง
    // (หากคุณมี Theme ของตัวเอง ให้เปลี่ยน SeatlyTheme เป็นชื่อ Theme ของคุณ)
    MaterialTheme {
        Column(Modifier.fillMaxSize().background(Color(0xFF121212))) {
            // 2.1 วางหน้า TicketsScreen โดยให้กินพื้นที่ที่เหลือทั้งหมด
            Box(modifier = Modifier.weight(1f)) {
                TicketsScreen(tickets = sampleTickets)
            }

            // 2.2 วางเฉพาะ NavigationBar ต่อท้ายด้านล่างสุด
            NavigationBar(
                containerColor = Color(0xFF1A1A1A) // <--- 1. ทำให้พื้นหลังเป็นสีดำเทา
            ) {
                // State สำหรับจำว่าปุ่มไหนถูกเลือก
                var currentDestination by remember { mutableStateOf(AppDestinations.TICKET) }

                AppDestinations.entries.forEach { destination ->
                    NavigationBarItem(
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                        selected = destination == currentDestination,
                        onClick = { currentDestination = destination },
                        // --- 2. เพิ่ม colors เข้าไปตรงนี้ ---
                        colors = NavigationBarItemDefaults.colors(
                            // --- สีของไอคอนและข้อความตอน "ไม่ได้เลือก" ---
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,

                            // --- สีของไอคอนและข้อความตอน "ถูกเลือก" (ให้เป็นสีแดงเหมือนเดิม) ---
                            selectedIconColor = Color(0xFFE50914),
                            selectedTextColor = Color(0xFFE50914),

                            // --- สีพื้นหลังของปุ่มตอน "ถูกเลือก" ---
                            indicatorColor = Color(0xFF2B2021)
                        )
                    )
                }
            }
        }
    }
}

