package com.flightinfo.app.model

data class Airport(
    val code: String,
    val name: String,
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
)
