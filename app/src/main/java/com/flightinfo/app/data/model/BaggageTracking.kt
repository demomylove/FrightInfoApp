package com.flightinfo.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(
    tableName = "baggage_items",
    indices = [
        Index("flight_number"),
        Index("baggage_tag_number"),
    ],
)
data class BaggageItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @SerializedName("baggage_tag_number")
    @ColumnInfo(name = "baggage_tag_number")
    val baggageTagNumber: String,

    @SerializedName("flight_number")
    @ColumnInfo(name = "flight_number")
    val flightNumber: String,

    @SerializedName("baggage_type")
    @ColumnInfo(name = "baggage_type")
    val baggageType: BaggageType,

    @SerializedName("weight")
    @ColumnInfo(name = "weight")
    val weight: Double,

    @SerializedName("weight_unit")
    @ColumnInfo(name = "weight_unit")
    val weightUnit: String = "kg",

    @SerializedName("status")
    @ColumnInfo(name = "status")
    val status: BaggageStatus,

    @SerializedName("last_location")
    @ColumnInfo(name = "last_location")
    val lastLocation: String?,

    @SerializedName("last_updated")
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Date = Date(),

    @SerializedName("check_in_time")
    @ColumnInfo(name = "check_in_time")
    val checkInTime: Date?,

    @SerializedName("loaded_time")
    @ColumnInfo(name = "loaded_time")
    val loadedTime: Date?,

    @SerializedName("unloaded_time")
    @ColumnInfo(name = "unloaded_time")
    val unloadedTime: Date?,

    @SerializedName("carousel_number")
    @ColumnInfo(name = "carousel_number")
    val carouselNumber: String?,

    @SerializedName("special_handling")
    @ColumnInfo(name = "special_handling")
    val specialHandling: Boolean = false,

    @SerializedName("notes")
    @ColumnInfo(name = "notes")
    val notes: String? = null,
)

enum class BaggageType {
    CHECKED,
    CARRY_ON,
    OVERSIZED,
    SPECIAL,
    FRAGILE,
}

enum class BaggageStatus {
    CHECKED_IN,
    LOADED,
    IN_TRANSIT,
    UNLOADED,
    ON_CAROUSEL,
    DELIVERED,
    DELAYED,
    LOST,
    DAMAGED,
}

data class BaggageTrackingResponse(
    @SerializedName("baggage_items")
    val baggageItems: List<BaggageItem>,

    @SerializedName("flight_info")
    val flightInfo: FlightInfo?,

    @SerializedName("airport_info")
    val airportInfo: AirportBaggageInfo?,
)

data class AirportBaggageInfo(
    @SerializedName("airport_code")
    val airportCode: String,

    @SerializedName("airport_name")
    val airportName: String,

    @SerializedName("baggage_claim_area")
    val baggageClaimArea: String,

    @SerializedName("carousel_numbers")
    val carouselNumbers: List<String>,

    @SerializedName("estimated_delivery_time")
    val estimatedDeliveryTime: String?,
)

data class BaggageTrackingRequest(
    @SerializedName("baggage_tag_number")
    val baggageTagNumber: String,

    @SerializedName("flight_number")
    val flightNumber: String,

    @SerializedName("passenger_name")
    val passengerName: String?,
)

data class BaggageUpdateRequest(
    @SerializedName("baggage_tag_number")
    val baggageTagNumber: String,

    @SerializedName("status")
    val status: BaggageStatus,

    @SerializedName("location")
    val location: String?,

    @SerializedName("notes")
    val notes: String?,
)
