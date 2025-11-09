package com.example.seatly.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Theaters
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.seatly.model.Movie
import com.example.seatly.ui.screens.MovieListItem

// เปลี่ยนชื่อ SuccessBody เป็นชื่อที่สื่อความหมายมากขึ้น เช่น HomeScreenBody
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

// อาจจะต้องสร้าง Composable MovieListItem ที่นี่ด้วย
@Composable
fun MovieListItem(movie: Movie, imgPath: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp), // กำหนดความสูงของ Card แต่ละอัน
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. ส่วนของรูปภาพโปสเตอร์
            AsyncImage(
                model = imgPath,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp) // กำหนดความกว้างของรูป
            )

            // 2. ส่วนของข้อมูลหนัง
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceEvenly // จัดให้ข้อมูลกระจายตัวสวยงาม
            ) {
                // ชื่อหนัง
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis // ใส่ ... ถ้ายาวเกิน
                )

                // เนื้อเรื่องย่อ
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis // ใส่ ... ถ้ายาวเกิน
                )

                // วันที่เข้าฉาย
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Release Date",
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = movie.releaseDate ?: "N/A",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}