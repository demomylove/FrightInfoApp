package com.flightinfo.app.data.model

import com.google.gson.annotations.SerializedName

data class CarbonFootprint(
    @SerializedName("co2_emission_kg")
    val co2EmissionKg: Double,
) {
    fun getEnvironmentalImpact(): EnvironmentalImpact {
        return when {
            co2EmissionKg < 100 -> EnvironmentalImpact.LOW
            co2EmissionKg <= 500 -> EnvironmentalImpact.MEDIUM
            else -> EnvironmentalImpact.HIGH
        }
    }
}
