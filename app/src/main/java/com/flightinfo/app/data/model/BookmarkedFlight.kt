package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_flights")
data class BookmarkedFlight(
    @PrimaryKey
    val flightNumber: String,
    val airline: String,
    val departureAirport: String,
    val arrivalAirport: String,
    val departureTime: String,
    val arrivalTime: String,
    val status: String,
)
