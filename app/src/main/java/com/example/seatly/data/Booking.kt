package com.example.seatly.data

data class Booking(
    val hall: String,
    val date: String,
    val time: String,
    val row: String,
    val seats: String,
    val posterUrl: String? = null,
    val priceItems: List<Pair<String, Double>> = emptyList(),
    val total: Double = 0.0,
    val paymentMask: String = ""
){
    companion object {
        fun sample() = Booking(
            hall = "Hall1",
            date = "August 15, 2024",
            time = "19:30",
            row = "B",
            seats = "3,4",
            posterUrl = null,
            priceItems = listOf("Tickets(*2)" to 360.0, "Booking Fee" to 10.0, "Taxes" to 5.0),
            total = 375.0,
            paymentMask = "Visa •••• 1234"
        )
    }
}
