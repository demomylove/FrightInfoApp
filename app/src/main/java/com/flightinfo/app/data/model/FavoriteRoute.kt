package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_routes",
    indices = [Index(value = ["originAirport", "destinationAirport"], unique = true)],
)
data class FavoriteRoute(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val originAirport: String,
    val destinationAirport: String,
)
