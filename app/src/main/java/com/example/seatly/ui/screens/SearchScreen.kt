package com.example.seatly.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ผมได้ลบ enum class AppDestinations ที่ซ้ำซ้อนออกจากตรงนี้แล้ว

@Composable
fun SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val recentSearches = listOf("Spider", "Dune: Part Two", "The Batman")
    val trendingTopics = listOf("Spider", "New Release")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // สีพื้นหลังดำ
            .padding(16.dp)
    ) {
        // -- Title "Search" --
        Text(
            text = "Search",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif, // ใช้ฟอนต์ Serif เพื่อให้เหมือนในรูป
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        // -- Search Bar --
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().height(40.dp),
            placeholder = { Text("", color = Color.Gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Search",
                            tint = Color.Gray
                        )
                    }
                }
            },
            shape = CircleShape, // ทำให้ขอบมนสุดๆ
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.DarkGray,
                unfocusedBorderColor = Color.DarkGray,
                focusedContainerColor = Color(0xFF2A2A2A),
                unfocusedContainerColor = Color(0xFF2A2A2A),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // -- Recent Search Section --
        Text(
            text = "Recent Search",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(16.dp))
        // ใช้ Column ธรรมดาแทน LazyColumn สำหรับข้อมูลจำนวนน้อย
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            recentSearches.forEach { searchItem ->
                RecentSearchItem(text = searchItem)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // -- Trending Now Section --
        Text(
            text = "Trending Now",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            trendingTopics.forEach { topic ->
                TrendingTopicChip(text = topic)
            }
        }
    }
}

@Composable
fun RecentSearchItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = "Recent Search Icon",
            tint = Color.Gray
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

@Composable
fun TrendingTopicChip(text: String) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFF2A2A2A))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_4") // เพิ่ม device เพื่อให้เห็นภาพชัดขึ้น
@Composable
fun SearchScreenWithNavbarPreview() { // ตั้งชื่อใหม่ให้สื่อความหมาย

    // --- สร้างโครงสร้าง UI สำหรับ Preview ---
    MaterialTheme {
        Column(Modifier.fillMaxSize().background(Color(0xFF121212))) {
            // 1. วางหน้า SearchScreen โดยให้กินพื้นที่ที่เหลือทั้งหมด
            Box(modifier = Modifier.weight(1f)) {
                SearchScreen()
            }

            // 2. วางเฉพาะ NavigationBar ต่อท้ายด้านล่างสุด
            NavigationBar(
                containerColor = Color(0xFF1A1A1A) // ทำให้พื้นหลังเป็นสีดำเทา
            ) {
                // State สำหรับจำว่าปุ่มไหนถูกเลือก (เพื่อให้ Preview กดสลับได้)
                var currentDestination by remember { mutableStateOf(AppDestinations.SEARCH) }

                // เราต้อง import AppDestinations เข้ามาในไฟล์นี้ก่อน
                // โดยเพิ่ม import com.example.seatly.ui.screens.AppDestinations ที่ด้านบนของไฟล์
                AppDestinations.entries.forEach { destination ->
                    NavigationBarItem(
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                        selected = destination == currentDestination,
                        onClick = { currentDestination = destination },
                        colors = NavigationBarItemDefaults.colors(
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            selectedIconColor = Color(0xFFE50914),
                            selectedTextColor = Color(0xFFE50914),
                            indicatorColor = Color(0xFF2B2021)
                        )
                    )
                }
            }
        }
    }
}
