package com.flightinfo.app.data.repository

import com.flightinfo.app.data.dao.HistoricalFlightDao
import com.flightinfo.app.data.model.FlightInfo
import com.flightinfo.app.data.model.HistoricalFlight
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoricalFlightRepository @Inject constructor(
    private val historicalFlightDao: HistoricalFlightDao,
) {

    fun getHistoricalFlights(): Flow<List<HistoricalFlight>> {
        return historicalFlightDao.getHistoricalFlights()
    }

    suspend fun addHistoricalFlight(flightInfo: FlightInfo) {
        val historicalFlight = HistoricalFlight(
            flightNumber = flightInfo.flightNumber,
            airline = flightInfo.airline,
            departureAirport = flightInfo.departureAirport,
            arrivalAirport = flightInfo.arrivalAirport,
            departureTime = flightInfo.departureTime,
            arrivalTime = flightInfo.arrivalTime,
            status = flightInfo.status,
        )
        historicalFlightDao.insert(historicalFlight)
    }

    suspend fun clearHistory() {
        historicalFlightDao.clearHistory()
    }
}
