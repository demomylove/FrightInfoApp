package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historical_flights")
data class HistoricalFlight(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val flightNumber: String,
    val airline: String,
    val departureAirport: String,
    val arrivalAirport: String,
    val departureTime: String,
    val arrivalTime: String,
    val status: String,
    val accessedTimestamp: Long = System.currentTimeMillis(),
)
