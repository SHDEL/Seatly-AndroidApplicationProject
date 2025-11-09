package com.example.seatly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.seatly.data.Booking
import com.example.seatly.model.Movie
import com.example.seatly.ui.theme.SeatlyTheme
import com.example.seatly.ui.screens.BookingSummaryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SeatlyTheme(darkTheme = true) {
                SeatlyApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun SeatlyApp() {
    // 1. สร้าง State เพื่อจำลองการเปลี่ยนหน้า
    var showBookingSummary by rememberSaveable { mutableStateOf(false) }

    // 2. ใช้ if-else ในการเลือกว่าจะแสดงหน้าจอไหน
    if (showBookingSummary) {
        // --- กรณีที่ต้องแสดงหน้า Booking Summary ---
        BookingSummaryScreen(
            movie = Movie(1, "Spider", "", "/poster.jpg", "", "", 8.4),
            booking = Booking.sample(),
            modifier = Modifier.fillMaxSize(),
            onBack = {
                // เมื่อกดปุ่ม Back ในหน้า Summary ให้กลับไปหน้าหลัก
                showBookingSummary = false
            }
        )
    } else {
        // --- กรณีที่แสดงหน้าจอปกติ (แต่ตอนนี้จะไม่มี Navbar แล้ว) ---
        Scaffold(
            modifier = Modifier.fillMaxSize()
            // --- เราได้ลบ bottomBar ทั้งหมดออกจากตรงนี้แล้ว ---
        ) { innerPadding ->
            // ใส่เนื้อหาของหน้าจอต่างๆ ที่นี่
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Now showing: Home Screen")

                // เพิ่มปุ่มเพื่อใช้ทดสอบการไปหน้า Booking Summary
                Button(onClick = { showBookingSummary = true }) {
                    Text("Go to Booking Summary")
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SeatlyTheme {
        Greeting("Android")
    }
}
