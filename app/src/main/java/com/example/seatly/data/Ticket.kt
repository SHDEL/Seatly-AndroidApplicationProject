package com.example.seatly.data

import com.example.seatly.model.Movie
import com.example.seatly.data.Booking

data class Ticket(
    val movie: Movie,
    val booking: Booking
)
