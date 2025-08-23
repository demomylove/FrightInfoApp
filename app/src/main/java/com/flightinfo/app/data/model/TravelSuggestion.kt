package com.flightinfo.app.data.model

data class TravelSuggestion(
    val destination: String,
    val description: String = "",
    val tips: List<String> = emptyList(),
)
