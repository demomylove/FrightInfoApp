package com.flightinfo.app.repository

import com.flightinfo.app.model.Airport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AirportRepository @Inject constructor() {

    private val airports = listOf(
        Airport("JFK", "John F. Kennedy International Airport", "New York", "United States", 40.641766, -73.780968),
        Airport("LAX", "Los Angeles International Airport", "Los Angeles", "United States", 33.942791, -118.410042),
        Airport("ORD", "O'Hare International Airport", "Chicago", "United States", 41.978611, -87.904724),
        Airport("DFW", "Dallas/Fort Worth International Airport", "Dallas", "United States", 32.897480, -97.040443),
        Airport("DEN", "Denver International Airport", "Denver", "United States", 39.849312, -104.673828),
        Airport("SFO", "San Francisco International Airport", "San Francisco", "United States", 37.615223, -122.389977),
        Airport("SEA", "Seattle-Tacoma International Airport", "Seattle", "United States", 47.449001, -122.308998),
        Airport("LAS", "McCarran International Airport", "Las Vegas", "United States", 36.086010, -115.153969),
        Airport("PHX", "Phoenix Sky Harbor International Airport", "Phoenix", "United States", 33.435302, -112.005905),
        Airport("IAH", "George Bush Intercontinental Airport", "Houston", "United States", 29.993067, -95.341812),
        Airport("LHR", "London Heathrow Airport", "London", "United Kingdom", 51.470020, -0.454295),
        Airport("CDG", "Charles de Gaulle Airport", "Paris", "France", 49.009724, 2.547778),
        Airport("AMS", "Amsterdam Airport Schiphol", "Amsterdam", "Netherlands", 52.308056, 4.764167),
        Airport("FRA", "Frankfurt Airport", "Frankfurt", "Germany", 50.0379, 8.5622),
        Airport("MAD", "Madrid-Barajas Airport", "Madrid", "Spain", 40.47222, -3.56083),
        Airport("BCN", "Barcelona-El Prat Airport", "Barcelona", "Spain", 41.29694, 2.07833),
        Airport("FCO", "Leonardo da Vinci International Airport", "Rome", "Italy", 41.8002778, 12.2388889),
        Airport("MUC", "Munich Airport", "Munich", "Germany", 48.35389, 11.78611),
        Airport("ZRH", "Zurich Airport", "Zurich", "Switzerland", 47.451542, 8.564572),
        Airport("IST", "Istanbul Airport", "Istanbul", "Turkey", 41.276901, 28.729324),
        Airport("DXB", "Dubai International Airport", "Dubai", "United Arab Emirates", 25.2528, 55.3644),
        Airport("SIN", "Singapore Changi Airport", "Singapore", "Singapore", 1.3502, 103.9940),
        Airport("HKG", "Hong Kong International Airport", "Hong Kong", "Hong Kong", 22.3080, 113.9185),
        Airport("NRT", "Narita International Airport", "Tokyo", "Japan", 35.7647, 140.3860),
        Airport("ICN", "Incheon International Airport", "Seoul", "South Korea", 37.4602, 126.4407),
        Airport("BKK", "Suvarnabhumi Airport", "Bangkok", "Thailand", 13.6900, 100.7501),
        Airport("KUL", "Kuala Lumpur International Airport", "Kuala Lumpur", "Malaysia", 2.7433, 101.6981),
        Airport("SYD", "Sydney Kingsford Smith Airport", "Sydney", "Australia", -33.9461, 151.1770),
        Airport("MEL", "Melbourne Airport", "Melbourne", "Australia", -37.6615, 144.8364),
        Airport("YYZ", "Toronto Pearson International Airport", "Toronto", "Canada", 43.6772, -79.6306),
        Airport("YVR", "Vancouver International Airport", "Vancouver", "Canada", 49.1902, -123.1837),
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
