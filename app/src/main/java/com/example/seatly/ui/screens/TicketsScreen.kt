package com.example.seatly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.seatly.model.Booking
import com.example.seatly.model.Ticket
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
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
fun AppNavigationContainer() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.TICKET) }

    // ข้อมูลตัวอย่างสำหรับแสดงผล (ในแอปจริงควรมาจาก ViewModel)
    val movie1 = Movie(1, listOf(1),"Spider-Man: Across the Spider-Verse", "...", "/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg", "", "", 8.4)
    val movie2 = Movie(2, listOf(1),"Dune: Part Two", "...", "/1m02V5NF2l2b1TfLkrrydrw1hEu.jpg", "", "", 8.3)

    val sampleMovies = listOf(movie1, movie2)
    val sampleTickets = listOf(
        Ticket(
            movie = Movie(1, listOf(1),"Spider-Man: Across the Spider-Verse", "...", "/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg", "", "", 8.4),
            booking = Booking(hall = "Cinema 5", date = "SUN, MAR 9, 2025", time = "14:30", row = "B", seats = "B2, B3")
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // ใช้ Component NavigationBar ที่สร้างขึ้นใหม่
            AppNavigationBar(
                currentDestination = currentDestination,
                onNavigate = { destination -> currentDestination = destination }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentDestination) {
                AppDestinations.HOME -> HomeScreenBody(movies = sampleMovies)
                AppDestinations.SEARCH -> SearchScreen()
                AppDestinations.TICKET -> TicketsScreen(tickets = sampleTickets)
                AppDestinations.PROFILE -> ProfileScreenContent()
            }
        }
    }
}

@Composable
fun ProfileScreenContent() {
    // สร้างหน้าจอว่างๆ สำหรับ Profile ไปก่อน
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // ใช้สีเดียวกับหน้าอื่น
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Profile Screen",
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Composable
fun TicketsScreen(
    tickets: List<Ticket>,
    modifier: Modifier = Modifier,
    onTicketClick: (Ticket) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Active, 1 = Past
    val shownTickets = if (selectedTab == 0) tickets else tickets.reversed() // ตัวอย่างการกรอง

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- ส่วนประกอบที่ 1: Top Bar ---
        TicketsTopBar()
        Spacer(Modifier.height(16.dp))

        // --- ส่วนประกอบที่ 2: แถบเลือกประเภทตั๋ว ---
        TicketTypeTabs(
            selectedTabIndex = selectedTab,
            onTabSelected = { newIndex -> selectedTab = newIndex }
        )
        Spacer(Modifier.height(16.dp))

        // --- ส่วนประกอบที่ 3: รายการตั๋ว ---
        if (shownTickets.isEmpty()) {
            EmptyState(message = if (selectedTab == 0) "You have no active tickets." else "You have no past tickets.")
        } else {
            TicketsList(tickets = shownTickets, onTicketClick = onTicketClick)
        }
    }
}

@Composable
private fun TicketsTopBar() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "My Tickets",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun TicketTypeTabs(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF2A2A2A),
        modifier = Modifier.fillMaxWidth().height(40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabButton(text = "Active", selected = selectedTabIndex == 0) { onTabSelected(0) }
            TabButton(text = "Past", selected = selectedTabIndex == 1) { onTabSelected(1) }
        }
    }
}

@Composable
private fun TicketsList(tickets: List<Ticket>, onTicketClick: (Ticket) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(tickets, key = { it.booking.date + it.movie.id }) { ticket ->
            TicketCard(ticket = ticket, onClick = { onTicketClick(ticket) })
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun RowScope.TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (selected) Color(0xFFE50914) else Color.Transparent
    val contentColor = if (selected) Color.White else Color.LightGray

    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
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
fun TicketCard(ticket: Ticket, onClick: () -> Unit = {}) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().height(200.dp).clickable { onClick() },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${ticket.movie.poster}",
                contentDescription = ticket.movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))
            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(ticket.movie.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(4.dp))
                Text("${ticket.booking.date} | ${ticket.booking.time}", color = Color.LightGray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(thickness = 1.dp, color = Color.Gray.copy(alpha = 0.5f))
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EventSeat, "Tickets", tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("${ticket.booking.seats.split(',').size} Tickets", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EventSeat, "Seats", tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(ticket.booking.seats, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigationBar(currentDestination: AppDestinations, onNavigate: (AppDestinations) -> Unit) {
    NavigationBar(
        containerColor = Color(0xFF1A1A1A)
    ) {
        AppDestinations.entries.forEach { destination ->
            SeatlyNavigationBarItem(
                destination = destination,
                currentDestination = currentDestination,
                onClick = { onNavigate(destination) }
            )
        }
    }
}

@Composable
fun RowScope.NavigationBarItem(
    destination: AppDestinations,
    currentDestination: AppDestinations,
    onClick: () -> Unit
) {
    NavigationBarItem(
        icon = { Icon(destination.icon, contentDescription = destination.label) },
        label = { Text(destination.label) },
        selected = destination == currentDestination,
        onClick = onClick,
        colors = NavigationBarItemDefaults.colors(
            unselectedIconColor = Color.Gray,
            unselectedTextColor = Color.Gray,
            selectedIconColor = Color(0xFFE50914),
            selectedTextColor = Color(0xFFE50914),
            indicatorColor = Color(0xFF2B2021)
        )
    )
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun TicketsScreenWithNavbarPreview() {
    val sampleTickets = listOf(
        Ticket(
            movie = Movie(1, listOf(1),"Spider-Man: Across the Spider-Verse", "...", "/8Vt6mWEReuy4Of61Lnj5Xj704m8.jpg", "", "", 8.4),
            booking = Booking(hall = "Cinema 5", date = "SUN, MAR 9, 2025", time = "14:30", row = "B", seats = "B2, B3")
        )
    )
    MaterialTheme {
        // ใช้ AppNavigationContainer ในการ Preview เพื่อให้เหมือนของจริงที่สุด
        AppNavigationContainer()
    }
}

