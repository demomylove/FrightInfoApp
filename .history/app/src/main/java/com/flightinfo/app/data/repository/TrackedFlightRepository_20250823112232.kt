package com.flightinfo.app.data.repository

import com.flightinfo.app.data.dao.TrackedFlightDao
import com.flightinfo.app.data.model.TrackedFlight
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackedFlightRepository @Inject constructor(
    private val trackedFlightDao: TrackedFlightDao
) {
    fun getAllTrackedFlights(): Flow<List<TrackedFlight>> {
        return trackedFlightDao.getAllTrackedFlights()
    }

    suspend fun getTrackedFlightById(flightId: String): TrackedFlight? {
        return trackedFlightDao.getTrackedFlightById(flightId)
    }

    suspend fun insertTrackedFlight(trackedFlight: TrackedFlight) {
        trackedFlightDao.insertTrackedFlight(trackedFlight)
    }

    suspend fun updateTrackedFlight(trackedFlight: TrackedFlight) {
        trackedFlightDao.updateTrackedFlight(trackedFlight)
    }

    suspend fun deleteTrackedFlight(trackedFlight: TrackedFlight) {
        trackedFlightDao.deleteTrackedFlight(trackedFlight)
    }

    suspend fun deleteTrackedFlightById(flightId: String) {
        trackedFlightDao.deleteTrackedFlightById(flightId)
    }
}