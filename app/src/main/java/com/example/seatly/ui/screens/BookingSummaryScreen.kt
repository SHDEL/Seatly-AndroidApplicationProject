package com.example.seatly.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.seatly.model.Movie
import com.example.seatly.ui.theme.SeatlyTheme

@Composable
fun BookingSummaryScreen(
    movie: Movie,
    booking: Booking,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onChangePayment: () -> Unit = {},
    onConfirmBooking: () -> Unit // Changed from onConfirm
) {
    var isTermsAgreed by remember { mutableStateOf(false) }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFF0F0F0F)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // --- ส่วนประกอบที่ 1: Top Bar ---
            BookingSummaryTopBar(onBack = onBack)

            // --- เนื้อหาหลักของหน้าจอ ---
            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(8.dp)) // ระยะห่างหลัง TopBar

                // --- ส่วนประกอบที่ 2: การ์ดข้อมูลหนัง ---
                MovieBookingDetailsCard(movie = movie, booking = booking)
                Spacer(modifier = Modifier.height(32.dp))

                // --- ส่วนประกอบที่ 3: การ์ดรายละเอียดราคา ---
                PriceBreakdownCard(booking = booking)
                Spacer(modifier = Modifier.height(32.dp))

                // --- ส่วนประกอบที่ 4: การ์ดวิธีชำระเงิน ---
                PaymentMethodCard(
                    paymentMask = booking.paymentMask,
                    onChangeClick = onChangePayment
                )
                Spacer(modifier = Modifier.height(12.dp))

                // --- ส่วนประกอบที่ 5: แถบข้อตกลงและเงื่อนไข ---
                TermsAndConditionsRow(
                    checked = isTermsAgreed,
                    onCheckedChange = { isTermsAgreed = it }
                )
                Spacer(modifier = Modifier.weight(1f)) // ดันปุ่มไปล่างสุด
                Spacer(modifier = Modifier.height(24.dp))

                // --- ส่วนประกอบที่ 6: ปุ่มยืนยันการจอง ---
                ConfirmBookingButton(
                    enabled = isTermsAgreed,
                    onClick = onConfirmBooking // Changed from onConfirm
                )
            }
        }
    }
}

@Composable
private fun BookingSummaryTopBar(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp) //ปรับ Padding
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier
                .size(28.dp) //ปรับขนาด
                .align(Alignment.CenterStart)
                .clickable(onClick = onBack)
        )
        Text(
            text = "Booking Summary",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun MovieBookingDetailsCard(movie: Movie, booking: Booking) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = movie.title, color = Color(0xFFE50914), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = booking.hall, color = Color.LightGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "${booking.date} | ${booking.time}", color = Color.LightGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Row ${booking.row} Seats ${booking.seats}", color = Color.LightGray, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Display movie poster
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${booking.posterUrl}",
                contentDescription = movie.title,
                modifier = Modifier
                    .size(width = 90.dp, height = 130.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun PriceBreakdownCard(booking: Booking) {
    Column {
        Text(text = "Price Breakdown", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 12.dp)) {
                booking.priceItems.forEach { (label, amount) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = label, color = Color.LightGray, fontSize = 14.sp)
                        Text(text = "฿${String.format("%.2f", amount)}", color = Color.LightGray, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Divider(color = Color.DarkGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Total Amount", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(text = "฿${String.format("%.2f", booking.total)}", color = Color(0xFFE50914), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodCard(paymentMask: String, onChangeClick: () -> Unit) {
    Column {
        Text(text = "Payment Method", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = "Card",
                        tint = Color(0xFFE50914),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = paymentMask, color = Color.White)
                }
                Text(
                    text = "Change",
                    color = Color(0xFFE50914),
                    modifier = Modifier.clickable(onClick = onChangeClick)
                )
            }
        }
    }
}

@Composable
private fun TermsAndConditionsRow(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onCheckedChange(!checked) } //ทำให้กดทั้งแถวได้
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFFE50914),
                uncheckedColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "I agree to the Terms & Conditions.", color = Color.LightGray, fontSize = 14.sp)
    }
}

@Composable
private fun ConfirmBookingButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled, //ใช้ State จากภายนอก
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE50914),
            disabledContainerColor = Color.DarkGray //สีปุ่มตอนกดไม่ได้
        )
    ) {
        Text(
            text = "Confirm Booking",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}


@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun BookingSummaryPreview() {
    SeatlyTheme(darkTheme = true) {
        val mockMovies =
            Movie(
                id = 1,
                genre = listOf(28, 12),
                title = "Spider-Man: Across the Spider-Verse",
                overview = "",
                poster = "/8Vt6mWEReuy4Of61Lp5Sj7ShVvL.jpg",
                backdrop = "",
                releaseDate = "2023-05-31",
                voteAverage = 8.4,
                genreName = listOf("Action", "Adventure", "Sci-Fi")
            )
        BookingSummaryScreen(
            movie = mockMovies,
            booking = Booking.sample(),
            onConfirmBooking = {}
        )
    }
}
