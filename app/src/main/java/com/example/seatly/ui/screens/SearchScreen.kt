package com.example.seatly.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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




@Composable//หน้ารวม components ย่อยทั้งหมด
fun SearchScreen() {
    // State และข้อมูลจะถูกจัดการที่นี่ และส่งต่อไปยัง Components ย่อย
    var searchQuery by remember { mutableStateOf("") }
    val recentSearches = listOf("Spider", "Dune: Part Two", "The Batman")
    val trendingTopics = listOf("Spider", "New Release")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // -- Title "Search" --
        Text(
            text = "Search",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        // -- Search Bar Component --
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        Spacer(Modifier.height(24.dp))

        // -- Recent Search Section Component --
        RecentSearchSection(
            searches = recentSearches,
            onItemClick = { clickedSearch ->
                searchQuery = clickedSearch // เมื่อกด จะให้ข้อความไปอยู่ในช่องค้นหา
            }
        )

        Spacer(Modifier.height(24.dp))

        // -- Trending Now Section Component --
        TrendingNowSection(
            topics = trendingTopics,
            onTopicClick = { clickedTopic ->
                searchQuery = clickedTopic // เมื่อกด จะให้ข้อความไปอยู่ในช่องค้นหา
            }
        )
    }
}

@Composable //ส่วนของ Search Bar ที่ใช้พิมพ์ค้นหา
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth(), //ปรับแก้ความสูง
        placeholder = { Text("", color = Color.Gray) }, //ปรับแก้ Text
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) { // เมื่อกดปุ่ม X จะล้างข้อความ
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Search",
                        tint = Color.Gray
                    )
                }
            }
        },
        shape = CircleShape,
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
}

@Composable//ส่วนของ recent search(ใช้แสดงข้อความที่เคยค้นหา)
private fun RecentSearchSection(searches: List<String>, onItemClick: (String) -> Unit) {
    Column {
        SectionHeader(title = "Recent Search")
        Spacer(Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            searches.forEach { searchItem ->
                RecentSearchItem(text = searchItem, onClick = { onItemClick(searchItem) })
            }
        }
    }
}

@Composable //ส่วนของ trending now(ใช้แสดงหัวข้อที่กำลังเป็นที่นิยม)
private fun TrendingNowSection(topics: List<String>, onTopicClick: (String) -> Unit) {
    Column {
        SectionHeader(title = "Trending Now")
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            topics.forEach { topic ->
                TrendingTopicChip(text = topic, onClick = { onTopicClick(topic) })
            }
        }
    }
}

@Composable //หัวข้อใหญ่ Recent Search กับ Trending Now
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
    )
}

@Composable // Component สำหรับแสดงรายการค้นหาล่าสุด
private fun RecentSearchItem(text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // ทำให้กดได้
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = "Recent Search Icon",
            tint = Color.Gray
        )
        Text(text = text, color = Color.White, fontSize = 16.sp)
    }
}

@Composable // Component สำหรับแสดงหัวข้อที่กำลังเป็นที่นิยม
private fun TrendingTopicChip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFF2A2A2A))
            .clickable(onClick = onClick) // ทำให้กดได้
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontSize = 14.sp)
    }
}

@Composable // Component สำหรับ NavigationBarItem
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

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun SearchScreenWithNavbarPreview() {
    MaterialTheme {
        Column(Modifier.fillMaxSize().background(Color(0xFF121212))) {
            //SearchScreen
            Box(modifier = Modifier.weight(1f)) {
                SearchScreen()
            }

            //NavigationBar
            NavigationBar(
                containerColor = Color(0xFF1A1A1A)
            ) {
                // State สำหรับจำว่าปุ่มไหนถูกเลือก
                var currentDestination by remember { mutableStateOf(AppDestinations.SEARCH) }

                AppDestinations.entries.forEach { destination ->
                    SeatlyNavigationBarItem(
                        destination = destination,
                        currentDestination = currentDestination,
                        onClick = { newDestination ->
                            currentDestination = newDestination
                        }
                    )
                }
            }
        }
    }
}
