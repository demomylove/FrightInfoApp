package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "flight_schedules")
data class FlightSchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val flightNumber: String,
    val airline: String,
    val departureAirport: String,
    val departureAirportCode: String,
    val arrivalAirport: String,
    val arrivalAirportCode: String,
    val departureTime: String,
    val arrivalTime: String,
    val duration: Int,
    val daysOfWeek: List<Int>,
    val aircraftType: String?,
    val isDomestic: Boolean,
    val lastUpdated: Long = System.currentTimeMillis(),
)

@Entity(tableName = "airline_info")
data class AirlineInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String,
    val name: String,
    val logoUrl: String?,
    val country: String,
    val lastUpdated: Long = System.currentTimeMillis(),
)

@Entity(tableName = "offline_airport_info")
data class OfflineAirportInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val iataCode: String,
    val icaoCode: String?,
    val name: String,
    val city: String,
    val country: String,
    val latitude: Double?,
    val longitude: Double?,
    val timezone: String?,
    val lastUpdated: Long = System.currentTimeMillis(),
)

@Entity(tableName = "downloaded_schedule_packages")
data class DownloadedSchedulePackage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packageName: String,
    val description: String,
    val downloadDate: Long,
    val fileSize: Long,
    val version: String,
    val isActive: Boolean = true,
)

class ScheduleConverters {
    @TypeConverter
    fun fromDaysOfWeekList(days: List<Int>): String {
        return Gson().toJson(days)
    }

    @TypeConverter
    fun toDaysOfWeekList(daysString: String): List<Int> {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(daysString, listType)
    }
}
