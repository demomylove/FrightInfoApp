package com.flightinfo.app.data.model

import com.google.gson.annotations.SerializedName

data class FlightInfo(
    @SerializedName("flight_number")
    val flightNumber: String,

    @SerializedName("airline")
    val airline: String,

    @SerializedName("departure_airport")
    val departureAirport: String,

    @SerializedName("arrival_airport")
    val arrivalAirport: String,

    @SerializedName("departure_time")
    val departureTime: String,

    @SerializedName("arrival_time")
    val arrivalTime: String,

    @SerializedName("scheduled_departure")
    val scheduledDeparture: String?,

    @SerializedName("actual_departure")
    val actualDeparture: String?,

    @SerializedName("status")
    val status: String,

    @SerializedName("gate")
    val gate: String?,

    @SerializedName("terminal")
    val terminal: String?,

    @SerializedName("delay")
    val delay: Int = 0,

    @SerializedName("aircraft_type")
    val aircraftType: String?,

    @SerializedName("carbon_footprint")
    val carbonFootprint: CarbonFootprint?,

    @SerializedName("price")
    val price: Double?,

    @SerializedName("currency")
    val currency: String? = "USD",
)

data class FlightSearchResponse(
    @SerializedName("flights")
    val flights: List<FlightInfo>,

    @SerializedName("total_count")
    val totalCount: Int,
)

data class FlightSearchRequest(
    val flightNumber: String?,
    val departureAirport: String?,
    val arrivalAirport: String?,
    val date: String?,
)
