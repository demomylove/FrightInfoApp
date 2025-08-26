package com.flightinfo.app.data.repository

import com.flightinfo.app.data.dao.BookmarkedFlightDao
import com.flightinfo.app.data.model.BookmarkedFlight
import com.flightinfo.app.data.model.FlightInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkedFlightRepository @Inject constructor(
    private val bookmarkedFlightDao: BookmarkedFlightDao
) {

    fun getAllBookmarkedFlights(): Flow<List<BookmarkedFlight>> {
        return bookmarkedFlightDao.getAllBookmarkedFlights()
    }

    fun isBookmarked(flightNumber: String): Flow<Boolean> {
        return bookmarkedFlightDao.isBookmarked(flightNumber).map { it > 0 }
    }

    suspend fun addBookmark(flightInfo: FlightInfo) {
        val bookmarkedFlight = BookmarkedFlight(
            flightNumber = flightInfo.flightNumber,
            airline = flightInfo.airline,
            departureAirport = flightInfo.departureAirport,
            arrivalAirport = flightInfo.arrivalAirport,
            departureTime = flightInfo.departureTime,
            arrivalTime = flightInfo.arrivalTime,
            status = flightInfo.status
        )
        bookmarkedFlightDao.insert(bookmarkedFlight)
    }

    suspend fun removeBookmark(flightInfo: FlightInfo) {
        val bookmarkedFlight = BookmarkedFlight(
            flightNumber = flightInfo.flightNumber,
            airline = flightInfo.airline,
            departureAirport = flightInfo.departureAirport,
            arrivalAirport = flightInfo.arrivalAirport,
            departureTime = flightInfo.departureTime,
            arrivalTime = flightInfo.arrivalTime,
            status = flightInfo.status
        )
        bookmarkedFlightDao.delete(bookmarkedFlight)
    }
}
