package com.flightinfo.app.data.model

import com.google.gson.annotations.SerializedName

data class FlightPriceInfo(
    @SerializedName("flight_id")
    val flightId: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("currency")
    val currency: String,
)
