package com.example.seatly.ui.screens


import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.seatly.model.Movie
import com.example.seatly.ui.theme.SeatlyTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.seatly.data.Booking

@Composable
fun BookingSummaryScreen(
    movie: Movie,
    booking: Booking,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onChangePayment: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF0F0F0F)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(start = 8.dp)
                        .align(Alignment.CenterStart)
                        .clickable { onBack() }
                )

                Text(
                    text = "Booking Summary",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f).padding(8.dp)) {
                        Text(text = movie.title, color = Color(0xFFE50914), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = booking.hall, color = Color.LightGray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "${booking.date} | ${booking.time}", color = Color.LightGray, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "Row ${booking.row} Seats ${booking.seats}", color = Color.LightGray, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Mockup poster placeholder (no network)
                    Box(
                        modifier = Modifier
                            .size(width = 96.dp, height = 140.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Poster", color = Color.White, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Price Breakdown", color = Color.White, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    booking.priceItems.forEach { (label, amount) ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = label, color = Color.LightGray)
                            Text(text = String.format("%.2f", amount), color = Color.LightGray)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Total Amount", color = Color.White, fontWeight = FontWeight.Bold)
                        Text(text = String.format("%.2f", booking.total), color = Color(0xFFE50914), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "PaymentMethod",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
            )
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
                        Text(text = booking.paymentMask, color = Color.White)
                    }
                    Text(text = "Change", color = Color(0xFFE50914), modifier = Modifier.clickable { onChangePayment() })
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val (checked, setChecked) = remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checked, onCheckedChange = setChecked, colors = androidx.compose.material3.CheckboxDefaults.colors(checkedColor = Color(0xFFE50914)))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "I agree to the Terms & Conditions.", color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.fillMaxWidth().height(72.dp), contentAlignment = Alignment.Center) {
                Button(
                    onClick = { if (checked) onConfirm() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914))
                ) {
                    Text(text = "Confirm Booking", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingSummaryPreview() {
    SeatlyTheme(darkTheme = true) {
        BookingSummaryScreen(
            movie = Movie(1, "Spider", "", "/poster.jpg", "", "", 8.4),
            booking = Booking.sample()
        )
    }
}
