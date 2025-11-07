package com.example.seatly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seatly.ui.theme.SeatlyTheme

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Image(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFE50914)),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Justin Bieber",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Text(
            text = "justin.bieber@gmail.com",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        SeatlyGoldCard()

        Spacer(modifier = Modifier.height(32.dp))

        ProfileSection(title = "Account") {
            ProfileMenuItem(icon = Icons.Default.Person, text = "Edit Profile")
            ProfileMenuItem(icon = Icons.Default.History, text = "Booking History")
            ProfileMenuItem(icon = Icons.Default.CreditCard, text = "Payment Methods", isLastItem = true)
        }

        Spacer(modifier = Modifier.height(24.dp))

        ProfileSection(title = "App") {
            ProfileMenuItem(icon = Icons.Default.Notifications, text = "Notifications")
            ProfileMenuItem(icon = Icons.Default.Settings, text = "App Settings")
            ProfileMenuItem(icon = Icons.AutoMirrored.Filled.HelpOutline, text = "Help & Support")
            ProfileMenuItem(icon = Icons.AutoMirrored.Filled.ExitToApp, text = "Log out", isLastItem = true)
        }
    }
}

@Composable
fun SeatlyGoldCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = "Gold Tier", tint = Color(0xFFE50914))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Seatly Gold", fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.weight(1f))
                Text("45%", color = Color.Gray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.45f)
                        .height(8.dp)
                        .background(Color(0xFFE50914))
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "250 points to next reward",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ProfileSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, text: String, isLastItem: Boolean = false) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = text, tint = Color(0xFFE50914))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text, color = Color.White, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
        if (!isLastItem) {
            HorizontalDivider(color = Color(0xFF2D2D2D), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ProfileScreenPreview() {
    SeatlyTheme(darkTheme = true) {
        ProfileScreen()
    }
}
