package com.example.flighttracker

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FlightRepositoryImpl {

    // TODO: Replace with real API key and endpoint
    private val apiKey = "YOUR_API_KEY"
    private val baseUrl = "http://api.aviationstack.com/v1/flights"

    suspend fun getFlightInfo(flightNumber: String): String {
        return withContext(Dispatchers.IO) {
            try {
                // Simulated API call for now
                // In real implementation, use Retrofit/OkHttp to fetch data
                "Flight $flightNumber: On Time (Simulated)"
            } catch (e: Exception) {
                return@withContext "Error fetching flight info: ${e.message}"
            }
        }
    }
}
