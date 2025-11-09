package com.example.seatly.model

data class Seat(
    val id: String,
    val row: Int,
    val number: Int,
    var status: SeatStatus
)
enum class SeatStatus {
    Available,
    Selected,
    Reserved
}
