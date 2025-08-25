package com.flightinfo.app.data.repository

import com.flightinfo.app.data.dao.AirlineInfoDao
import com.flightinfo.app.data.dao.DownloadedSchedulePackageDao
import com.flightinfo.app.data.dao.FlightScheduleDao
import com.flightinfo.app.data.dao.OfflineAirportInfoDao
import com.flightinfo.app.data.model.AirlineInfo
import com.flightinfo.app.data.model.DownloadedSchedulePackage
import com.flightinfo.app.data.model.FlightSchedule
import com.flightinfo.app.data.model.OfflineAirportInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlightScheduleRepository @Inject constructor(
    private val flightScheduleDao: FlightScheduleDao,
    private val airlineInfoDao: AirlineInfoDao,
    private val offlineAirportInfoDao: OfflineAirportInfoDao,
    private val downloadedSchedulePackageDao: DownloadedSchedulePackageDao,
) {

    // Flight Schedule operations
    fun getAllSchedules(): Flow<List<FlightSchedule>> = flightScheduleDao.getAllSchedules()

    fun searchSchedules(query: String): Flow<List<FlightSchedule>> =
        flightScheduleDao.searchSchedules(query)

    fun getSchedulesByAirport(airportCode: String): Flow<List<FlightSchedule>> =
        flightScheduleDao.getSchedulesByAirport(airportCode)

    fun getSchedulesByRoute(departureCode: String, arrivalCode: String): Flow<List<FlightSchedule>> =
        flightScheduleDao.getSchedulesByRoute(departureCode, arrivalCode)

    fun getSchedulesForToday(): Flow<List<FlightSchedule>> = flow {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        emitAll(flightScheduleDao.getSchedulesByDay(today))
    }

    suspend fun insertSchedules(schedules: List<FlightSchedule>) {
        flightScheduleDao.insertSchedules(schedules)
    }

    suspend fun deleteAllSchedules() {
        flightScheduleDao.deleteAllSchedules()
    }

    fun getScheduleCount(): Flow<Int> = flightScheduleDao.getScheduleCount()

    // Airline Info operations
    fun getAllAirlines(): Flow<List<AirlineInfo>> = airlineInfoDao.getAllAirlines()

    suspend fun getAirlineByCode(code: String): AirlineInfo? =
        airlineInfoDao.getAirlineByCode(code)

    fun searchAirlines(query: String): Flow<List<AirlineInfo>> =
        airlineInfoDao.searchAirlines(query)

    suspend fun insertAirlines(airlines: List<AirlineInfo>) {
        airlineInfoDao.insertAirlines(airlines)
    }

    // Airport Info operations
    fun getAllAirports(): Flow<List<OfflineAirportInfo>> = offlineAirportInfoDao.getAllAirports()

    suspend fun getAirportByCode(code: String): OfflineAirportInfo? =
        offlineAirportInfoDao.getAirportByCode(code)

    fun searchAirports(query: String): Flow<List<OfflineAirportInfo>> =
        offlineAirportInfoDao.searchAirports(query)

    suspend fun insertAirports(airports: List<OfflineAirportInfo>) {
        offlineAirportInfoDao.insertAirports(airports)
    }

    // Downloaded Package operations
    fun getAllDownloadedPackages(): Flow<List<DownloadedSchedulePackage>> =
        downloadedSchedulePackageDao.getAllPackages()

    fun getActivePackages(): Flow<List<DownloadedSchedulePackage>> =
        downloadedSchedulePackageDao.getActivePackages()

    suspend fun addDownloadedPackage(packageInfo: DownloadedSchedulePackage): Long {
        return downloadedSchedulePackageDao.insertPackage(packageInfo)
    }

    suspend fun deletePackage(packageInfo: DownloadedSchedulePackage) {
        downloadedSchedulePackageDao.deletePackage(packageInfo)
    }

    suspend fun deleteAllPackages() {
        downloadedSchedulePackageDao.deleteAllPackages()
    }

    // Offline data management
    fun getOfflineDataStatus(): Flow<OfflineDataStatus> = flow {
        val scheduleCount = flightScheduleDao.getScheduleCount()
        val airlineCountFlow = getAllAirlines().map { it.size }
        val airportCountFlow = getAllAirports().map { it.size }
        val packageCountFlow = getAllDownloadedPackages().map { it.size }

        // Combine all flows using zip operator
        val combinedFlow = scheduleCount.zip(airlineCountFlow) { schedules, airlines ->
            Pair(schedules, airlines)
        }.zip(airportCountFlow) { (schedules, airlines), airports ->
            Triple(schedules, airlines, airports)
        }.zip(packageCountFlow) { (schedules, airlines, airports), packages ->
            OfflineDataStatus(
                hasFlightSchedules = schedules > 0,
                scheduleCount = schedules,
                airlineCount = airlines,
                airportCount = airports,
                packageCount = packages,
                lastUpdated = System.currentTimeMillis(),
            )
        }

        emitAll(combinedFlow)
    }

    // Search and filter operations
    fun searchFlights(
        departureCode: String? = null,
        arrivalCode: String? = null,
        airline: String? = null,
        dayOfWeek: Int? = null,
        query: String? = null,
    ): Flow<List<FlightSchedule>> = flow {
        var result = getAllSchedules()

        result = result.map { schedules ->
            var filtered = schedules

            // Filter by departure airport
            departureCode?.let { code ->
                filtered = filtered.filter { it.departureAirportCode.equals(code, ignoreCase = true) }
            }

            // Filter by arrival airport
            arrivalCode?.let { code ->
                filtered = filtered.filter { it.arrivalAirportCode.equals(code, ignoreCase = true) }
            }

            // Filter by airline
            airline?.let { al ->
                filtered = filtered.filter {
                    it.airline.contains(al, ignoreCase = true) ||
                        it.flightNumber.contains(al, ignoreCase = true)
                }
            }

            // Filter by day of week
            dayOfWeek?.let { day ->
                filtered = filtered.filter { it.daysOfWeek.contains(day) }
            }

            // Filter by search query
            query?.let { q ->
                filtered = filtered.filter {
                    it.flightNumber.contains(q, ignoreCase = true) ||
                        it.airline.contains(q, ignoreCase = true) ||
                        it.departureAirportCode.contains(q, ignoreCase = true) ||
                        it.arrivalAirportCode.contains(q, ignoreCase = true) ||
                        it.departureAirport.contains(q, ignoreCase = true) ||
                        it.arrivalAirport.contains(q, ignoreCase = true)
                }
            }

            filtered.sortedBy { it.departureTime }
        }

        emitAll(result)
    }

    // Get flight schedule by ID
    suspend fun getScheduleById(id: Long): FlightSchedule? {
        return getAllSchedules().map { schedules -> schedules.find { it.id == id } }.first()
    }

    // Update schedule
    suspend fun updateSchedule(schedule: FlightSchedule) {
        flightScheduleDao.updateSchedule(schedule)
    }

    // Delete schedule
    suspend fun deleteSchedule(schedule: FlightSchedule) {
        flightScheduleDao.deleteSchedule(schedule)
    }
}

data class OfflineDataStatus(
    val hasFlightSchedules: Boolean,
    val scheduleCount: Int,
    val airlineCount: Int,
    val airportCount: Int,
    val packageCount: Int,
    val lastUpdated: Long,
)
