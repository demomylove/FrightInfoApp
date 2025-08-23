package com.flightinfo.app.data.model

import com.google.gson.annotations.SerializedName

data class TravelSuggestionResponse(
    @SerializedName("suggestions")
    val suggestions: List<TravelSuggestion> = emptyList(),
)
