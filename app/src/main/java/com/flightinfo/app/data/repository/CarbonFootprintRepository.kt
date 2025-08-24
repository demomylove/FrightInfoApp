package com.flightinfo.app.data.repository

import com.flightinfo.app.data.model.CarbonFootprint
import com.flightinfo.app.data.model.CarbonFootprintStats
import com.flightinfo.app.data.model.FlightInfo
import com.flightinfo.app.data.model.MonthlyEmission
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarbonFootprintRepository @Inject constructor() {

    private val aircraftEmissionFactors = mapOf(
        "A320" to 0.082,
        "A321" to 0.085,
        "A330" to 0.095,
        "A350" to 0.088,
        "B737" to 0.080,
        "B747" to 0.105,
        "B777" to 0.098,
        "B787" to 0.085,
        "E190" to 0.078,
        "CRJ" to 0.085,
    )

    private val airportDistances = mapOf(
        "PEK-PVG" to 1088.0,
        "PVG-CAN" to 1213.0,
        "CAN-PEK" to 1877.0,
        "PEK-LAX" to 10213.0,
        "SFO-NRT" to 8265.0,
        "LHR-JFK" to 5570.0,
        "CDG-DXB" to 5245.0,
        "SYD-SIN" to 6290.0,
    )

    fun calculateCarbonFootprint(flight: FlightInfo): CarbonFootprint {
        val distance = calculateDistance(flight.departureAirport, flight.arrivalAirport)
        val aircraftType = flight.aircraftType ?: "B737"
        val emissionFactor = aircraftEmissionFactors[aircraftType] ?: 0.085

        val co2Emission = distance * emissionFactor

        return CarbonFootprint(
            co2EmissionKg = co2Emission,
            distanceKm = distance,
            aircraftType = aircraftType,
            emissionFactor = emissionFactor,
        )
    }

    private fun calculateDistance(departure: String, arrival: String): Double {
        val key = "$departure-$arrival"
        return airportDistances[key] ?: calculateHaversineDistance(departure, arrival)
    }

    private fun calculateHaversineDistance(departure: String, arrival: String): Double {
        val airportCoordinates = mapOf(
            "PEK" to Pair(40.0799, 116.6031),
            "PVG" to Pair(31.1443, 121.8083),
            "CAN" to Pair(23.3924, 113.2988),
            "LAX" to Pair(33.9425, -118.4081),
            "SFO" to Pair(37.7749, -122.4194),
            "NRT" to Pair(35.7720, 140.3929),
            "LHR" to Pair(51.4700, -0.4543),
            "JFK" to Pair(40.6413, -73.7781),
            "CDG" to Pair(49.0097, 2.5479),
            "DXB" to Pair(25.2532, 55.3657),
            "SYD" to Pair(-33.9461, 151.1772),
            "SIN" to Pair(1.3644, 103.9915),
        )

        val depCoords = airportCoordinates[departure] ?: Pair(0.0, 0.0)
        val arrCoords = airportCoordinates[arrival] ?: Pair(0.0, 0.0)

        return if (depCoords == Pair(0.0, 0.0) || arrCoords == Pair(0.0, 0.0)) {
            1000.0
        } else {
            haversine(depCoords.first, depCoords.second, arrCoords.first, arrCoords.second)
        }
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
            Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c
    }

    fun calculateStatistics(flights: List<FlightInfo>): CarbonFootprintStats {
        val totalEmissions = flights.sumOf { it.carbonFootprint?.co2EmissionKg ?: 0.0 }
        val totalDistance = flights.sumOf { it.carbonFootprint?.distanceKm ?: 0.0 }
        val averageEmission = if (flights.isNotEmpty()) totalEmissions / flights.size else 0.0

        val airlineBreakdown = flights.groupBy { it.airline }
            .mapValues { (_, flightList) ->
                flightList.sumOf { it.carbonFootprint?.co2EmissionKg ?: 0.0 }
            }

        val monthlyTrend = generateMonthlyTrend(flights)

        return CarbonFootprintStats(
            totalEmissionsKg = totalEmissions,
            totalDistanceKm = totalDistance,
            flightCount = flights.size,
            averageEmissionPerFlight = averageEmission,
            monthlyTrend = monthlyTrend,
            airlineBreakdown = airlineBreakdown,
        )
    }

    private fun generateMonthlyTrend(flights: List<FlightInfo>): List<MonthlyEmission> {
        val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val currentMonth = java.time.LocalDate.now().monthValue - 1

        return (0..11).map { i ->
            val monthIndex = (currentMonth - i + 12) % 12
            val monthName = monthNames[monthIndex]
            val monthFlights = flights.filter { flight ->
                try {
                    val flightDate = java.time.LocalDate.parse(flight.departureTime)
                    flightDate.monthValue == monthIndex + 1
                } catch (e: Exception) {
                    false
                }
            }

            MonthlyEmission(
                month = monthName,
                emissionsKg = monthFlights.sumOf { it.carbonFootprint?.co2EmissionKg ?: 0.0 },
                flightCount = monthFlights.size,
            )
        }.reversed()
    }

    fun getCarbonOffsetSuggestions(co2EmissionKg: Double): List<CarbonOffsetSuggestion> {
        return listOf(
            CarbonOffsetSuggestion(
                type = "Tree Planting",
                description = "Plant trees to absorb CO2",
                action = "Plant ${(co2EmissionKg / 21.77).toInt()} trees",
                cost = 0.0,
                impact = "Long-term carbon sequestration",
            ),
            CarbonOffsetSuggestion(
                type = "Renewable Energy",
                description = "Support renewable energy projects",
                action = "Fund ${String.format("%.2f", co2EmissionKg * 0.15)} kWh of clean energy",
                cost = co2EmissionKg * 0.02,
                impact = "Immediate carbon reduction",
            ),
            CarbonOffsetSuggestion(
                type = "Carbon Credits",
                description = "Purchase certified carbon credits",
                action = "Buy ${String.format("%.2f", co2EmissionKg)} tons of carbon credits",
                cost = co2EmissionKg * 15.0,
                impact = "Certified carbon offset",
            ),
        )
    }
}

data class CarbonOffsetSuggestion(
    val type: String,
    val description: String,
    val action: String,
    val cost: Double,
    val impact: String,
)
