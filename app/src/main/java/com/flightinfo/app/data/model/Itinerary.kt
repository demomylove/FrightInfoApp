package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "itineraries")
data class Itinerary(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val pnr: String? = null,
    val passengers: List<String> = emptyList(),
    val startTime: Long,
    val endTime: Long,
    val segments: List<ItinerarySegment> = emptyList(),
    val tasks: List<TripTask> = emptyList(),
    val checkInUrl: String? = null,
    val boardingPassUrl: String? = null,
)

data class ItinerarySegment(
    val flightNumber: String,
    val airline: String?,
    val departureAirport: String,
    val arrivalAirport: String,
    val departureTime: Long,
    val arrivalTime: Long,
    val gate: String? = null,
    val terminal: String? = null,
)

data class TripTask(
    val id: String,
    val title: String,
    val dueTime: Long,
    val completed: Boolean = false,
)

class ItineraryConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromSegments(value: String?): List<ItinerarySegment> =
        if (value.isNullOrEmpty()) emptyList() else gson.fromJson(value, object : TypeToken<List<ItinerarySegment>>() {}.type)

    @TypeConverter
    fun toSegments(list: List<ItinerarySegment>?): String = gson.toJson(list ?: emptyList<ItinerarySegment>())

    @TypeConverter
    fun fromTasks(value: String?): List<TripTask> =
        if (value.isNullOrEmpty()) emptyList() else gson.fromJson(value, object : TypeToken<List<TripTask>>() {}.type)

    @TypeConverter
    fun toTasks(list: List<TripTask>?): String = gson.toJson(list ?: emptyList<TripTask>())
}
