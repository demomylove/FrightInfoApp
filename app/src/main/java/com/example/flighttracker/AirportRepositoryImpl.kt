package com.example.flighttracker

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Airport(
    val code: String,
    val name: String,
    val city: String,
    val country: String,
)

class AirportRepositoryImpl {

    private val airports = listOf(
        Airport("JFK", "John F. Kennedy International Airport", "New York", "United States"),
        Airport("LAX", "Los Angeles International Airport", "Los Angeles", "United States"),
        Airport("ORD", "O'Hare International Airport", "Chicago", "United States"),
        Airport("DFW", "Dallas/Fort Worth International Airport", "Dallas", "United States"),
        Airport("DEN", "Denver International Airport", "Denver", "United States"),
        Airport("SFO", "San Francisco International Airport", "San Francisco", "United States"),
        Airport("SEA", "Seattle-Tacoma International Airport", "Seattle", "United States"),
        Airport("LAS", "McCarran International Airport", "Las Vegas", "United States"),
        Airport("PHX", "Phoenix Sky Harbor International Airport", "Phoenix", "United States"),
        Airport("IAH", "George Bush Intercontinental Airport", "Houston", "United States"),
        Airport("LHR", "London Heathrow Airport", "London", "United Kingdom"),
        Airport("CDG", "Charles de Gaulle Airport", "Paris", "France"),
        Airport("AMS", "Amsterdam Airport Schiphol", "Amsterdam", "Netherlands"),
        Airport("FRA", "Frankfurt Airport", "Frankfurt", "Germany"),
        Airport("MAD", "Madrid-Barajas Airport", "Madrid", "Spain"),
        Airport("BCN", "Barcelona-El Prat Airport", "Barcelona", "Spain"),
        Airport("FCO", "Leonardo da Vinci International Airport", "Rome", "Italy"),
        Airport("MUC", "Munich Airport", "Munich", "Germany"),
        Airport("ZRH", "Zurich Airport", "Zurich", "Switzerland"),
        Airport("IST", "Istanbul Airport", "Istanbul", "Turkey"),
        Airport("DXB", "Dubai International Airport", "Dubai", "United Arab Emirates"),
        Airport("SIN", "Singapore Changi Airport", "Singapore", "Singapore"),
        Airport("HKG", "Hong Kong International Airport", "Hong Kong", "Hong Kong"),
        Airport("NRT", "Narita International Airport", "Tokyo", "Japan"),
        Airport("ICN", "Incheon International Airport", "Seoul", "South Korea"),
        Airport("BKK", "Suvarnabhumi Airport", "Bangkok", "Thailand"),
        Airport("KUL", "Kuala Lumpur International Airport", "Kuala Lumpur", "Malaysia"),
        Airport("SYD", "Sydney Kingsford Smith Airport", "Sydney", "Australia"),
        Airport("MEL", "Melbourne Airport", "Melbourne", "Australia"),
        Airport("YYZ", "Toronto Pearson International Airport", "Toronto", "Canada"),
        Airport("YVR", "Vancouver International Airport", "Vancouver", "Canada"),
    )

    suspend fun searchAirports(query: String): List<Airport> {
        return withContext(Dispatchers.Default) {
            if (query.isBlank()) {
                emptyList()
            } else {
                airports.filter { airport ->
                    airport.code.contains(query, ignoreCase = true) ||
                        airport.name.contains(query, ignoreCase = true) ||
                        airport.city.contains(query, ignoreCase = true) ||
                        airport.country.contains(query, ignoreCase = true)
                }.sortedWith(
                    compareBy<Airport> {
                        when {
                            it.code.equals(query, ignoreCase = true) -> -2
                            it.code.startsWith(query, ignoreCase = true) -> -1
                            else -> 0
                        }
                    }.thenBy { it.code },
                )
            }
        }
    }

    suspend fun getAirportByCode(code: String): Airport? {
        return withContext(Dispatchers.Default) {
            airports.find { it.code.equals(code, ignoreCase = true) }
        }
    }
}
