package com.flightinfo.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.flightinfo.app.data.model.AirlineInfo
import com.flightinfo.app.data.model.DownloadedSchedulePackage
import com.flightinfo.app.data.model.FlightSchedule
import com.flightinfo.app.data.model.OfflineAirportInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightScheduleDao {

    @Query("SELECT * FROM flight_schedules ORDER BY departureTime ASC")
    fun getAllSchedules(): Flow<List<FlightSchedule>>

    @Query("SELECT * FROM flight_schedules WHERE flightNumber LIKE :searchQuery OR airline LIKE :searchQuery OR departureAirportCode LIKE :searchQuery OR arrivalAirportCode LIKE :searchQuery")
    fun searchSchedules(searchQuery: String): Flow<List<FlightSchedule>>

    @Query("SELECT * FROM flight_schedules WHERE departureAirportCode = :airportCode OR arrivalAirportCode = :airportCode")
    fun getSchedulesByAirport(airportCode: String): Flow<List<FlightSchedule>>

    @Query("SELECT * FROM flight_schedules WHERE departureAirportCode = :departureCode AND arrivalAirportCode = :arrivalCode")
    fun getSchedulesByRoute(departureCode: String, arrivalCode: String): Flow<List<FlightSchedule>>

    @Query("SELECT * FROM flight_schedules WHERE daysOfWeek LIKE '%' || :dayOfWeek || '%'")
    fun getSchedulesByDay(dayOfWeek: Int): Flow<List<FlightSchedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<FlightSchedule>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: FlightSchedule): Long

    @Update
    suspend fun updateSchedule(schedule: FlightSchedule)

    @Delete
    suspend fun deleteSchedule(schedule: FlightSchedule)

    @Query("DELETE FROM flight_schedules")
    suspend fun deleteAllSchedules()

    @Query("SELECT COUNT(*) FROM flight_schedules")
    fun getScheduleCount(): Flow<Int>
}

@Dao
interface AirlineInfoDao {

    @Query("SELECT * FROM airline_info ORDER BY name ASC")
    fun getAllAirlines(): Flow<List<AirlineInfo>>

    @Query("SELECT * FROM airline_info WHERE code = :code")
    suspend fun getAirlineByCode(code: String): AirlineInfo?

    @Query("SELECT * FROM airline_info WHERE name LIKE :searchQuery OR code LIKE :searchQuery")
    fun searchAirlines(searchQuery: String): Flow<List<AirlineInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirlines(airlines: List<AirlineInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirline(airline: AirlineInfo): Long

    @Update
    suspend fun updateAirline(airline: AirlineInfo)

    @Delete
    suspend fun deleteAirline(airline: AirlineInfo)

    @Query("DELETE FROM airline_info")
    suspend fun deleteAllAirlines()
}

@Dao
interface OfflineAirportInfoDao {

    @Query("SELECT * FROM offline_airport_info ORDER BY name ASC")
    fun getAllAirports(): Flow<List<OfflineAirportInfo>>

    @Query("SELECT * FROM offline_airport_info WHERE iataCode = :code")
    suspend fun getAirportByCode(code: String): OfflineAirportInfo?

    @Query("SELECT * FROM offline_airport_info WHERE iataCode LIKE :searchQuery OR name LIKE :searchQuery OR city LIKE :searchQuery")
    fun searchAirports(searchQuery: String): Flow<List<OfflineAirportInfo>>

    @Query("SELECT * FROM offline_airport_info WHERE country = :country")
    fun getAirportsByCountry(country: String): Flow<List<OfflineAirportInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirports(airports: List<OfflineAirportInfo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAirport(airport: OfflineAirportInfo): Long

    @Update
    suspend fun updateAirport(airport: OfflineAirportInfo)

    @Delete
    suspend fun deleteAirport(airport: OfflineAirportInfo)

    @Query("DELETE FROM offline_airport_info")
    suspend fun deleteAllAirports()
}

@Dao
interface DownloadedSchedulePackageDao {

    @Query("SELECT * FROM downloaded_schedule_packages ORDER BY downloadDate DESC")
    fun getAllPackages(): Flow<List<DownloadedSchedulePackage>>

    @Query("SELECT * FROM downloaded_schedule_packages WHERE isActive = 1")
    fun getActivePackages(): Flow<List<DownloadedSchedulePackage>>

    @Query("SELECT * FROM downloaded_schedule_packages WHERE id = :id")
    suspend fun getPackageById(id: Long): DownloadedSchedulePackage?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackage(packageInfo: DownloadedSchedulePackage): Long

    @Update
    suspend fun updatePackage(packageInfo: DownloadedSchedulePackage)

    @Delete
    suspend fun deletePackage(packageInfo: DownloadedSchedulePackage)

    @Query("UPDATE downloaded_schedule_packages SET isActive = 0")
    suspend fun deactivateAllPackages()

    @Query("DELETE FROM downloaded_schedule_packages")
    suspend fun deleteAllPackages()
}
