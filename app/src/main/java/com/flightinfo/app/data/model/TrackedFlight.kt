package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracked_flights")
data class TrackedFlight(
    @PrimaryKey
    val flightId: String,
    val flightNumber: String,
    val lastStatus: String,
    val lastUpdated: Long,
    val notificationEnabled: Boolean
)

data class NotificationSettings(
    val delayNotifications: Boolean = true,
    val cancellationNotifications: Boolean = true,
    val gateChangeNotifications: Boolean = true,
    val doNotDisturbStart: String = "22:00",
    val doNotDisturbEnd: String = "07:00"
)