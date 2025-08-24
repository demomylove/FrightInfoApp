package com.flightinfo.app.data.model

data class CarbonFootprint(
    val co2EmissionKg: Double,
    val distanceKm: Double,
    val aircraftType: String,
    val passengerCount: Int = 1,
    val emissionFactor: Double,
    val calculationMethod: String = "ICAO",
) {
    fun getEmissionPerPassenger(): Double = co2EmissionKg / passengerCount

    fun getEmissionPerKm(): Double = co2EmissionKg / distanceKm

    fun getEnvironmentalImpact(): EnvironmentalImpact {
        return when {
            co2EmissionKg < 100 -> EnvironmentalImpact.LOW
            co2EmissionKg < 500 -> EnvironmentalImpact.MEDIUM
            else -> EnvironmentalImpact.HIGH
        }
    }

    fun getEquivalentCarKm(): Double = co2EmissionKg * 4.5

    fun getEquivalentTreesPlanted(): Int = (co2EmissionKg / 21.77).toInt()
}

enum class EnvironmentalImpact {
    LOW,
    MEDIUM,
    HIGH,
}

data class CarbonFootprintStats(
    val totalEmissionsKg: Double,
    val totalDistanceKm: Double,
    val flightCount: Int,
    val averageEmissionPerFlight: Double,
    val monthlyTrend: List<MonthlyEmission>,
    val airlineBreakdown: Map<String, Double>,
)

data class MonthlyEmission(
    val month: String,
    val emissionsKg: Double,
    val flightCount: Int,
)
