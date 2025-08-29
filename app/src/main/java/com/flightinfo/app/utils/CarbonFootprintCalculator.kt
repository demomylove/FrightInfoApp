package com.flightinfo.app.utils

import com.flightinfo.app.data.model.CarbonFootprint
import com.flightinfo.app.model.Airport
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object CarbonFootprintCalculator {

    private const val EMISSION_FACTOR_KG_PER_KM = 0.1
    private const val EARTH_RADIUS_KM = 6371.0

    fun calculate(departureAirport: Airport, arrivalAirport: Airport): CarbonFootprint {
        val distance = calculateDistance(departureAirport.latitude, departureAirport.longitude, arrivalAirport.latitude, arrivalAirport.longitude)
        val co2Kg = distance * EMISSION_FACTOR_KG_PER_KM
        return CarbonFootprint(co2EmissionKg = co2Kg)
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_KM * c
    }
}
