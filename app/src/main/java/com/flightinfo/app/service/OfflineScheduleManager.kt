package com.flightinfo.app.service

import android.content.Context
import android.util.Log
import com.flightinfo.app.data.model.AirlineInfo
import com.flightinfo.app.data.model.DownloadedSchedulePackage
import com.flightinfo.app.data.model.FlightSchedule
import com.flightinfo.app.data.model.OfflineAirportInfo
import com.flightinfo.app.data.repository.FlightScheduleRepository
import com.flightinfo.app.data.repository.OfflineDataStatus
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfflineScheduleManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val flightScheduleRepository: FlightScheduleRepository,
) {

    companion object {
        private const val TAG = "OfflineScheduleManager"
        private const val PREFS_NAME = "offline_schedule_prefs"
        private const val KEY_LAST_DOWNLOAD = "last_download_time"
        private const val KEY_AUTO_REFRESH_ENABLED = "auto_refresh_enabled"
        private const val REFRESH_INTERVAL = 24 * 60 * 60 * 1000L // 24 hours
    }

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Download schedules from network and cache locally
    suspend fun downloadAndCacheSchedules(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())

            // Simulate network download - in real app, this would call an API
            val mockSchedules = generateMockFlightSchedules()
            val mockAirlines = generateMockAirlines()
            val mockAirports = generateMockAirports()

            // Cache data locally
            flightScheduleRepository.insertSchedules(mockSchedules)
            flightScheduleRepository.insertAirlines(mockAirlines)
            flightScheduleRepository.insertAirports(mockAirports)

            // Update download time
            sharedPreferences.edit().putLong(KEY_LAST_DOWNLOAD, System.currentTimeMillis()).apply()

            // Create download package record
            val packageInfo = DownloadedSchedulePackage(
                packageName = "Default Flight Schedule",
                description = "Comprehensive flight schedule package",
                downloadDate = System.currentTimeMillis(),
                fileSize = 1024 * 1024L, // Mock file size
                version = "1.0.0",
            )
            flightScheduleRepository.addDownloadedPackage(packageInfo)

            Log.d(TAG, "Successfully cached ${mockSchedules.size} flight schedules")
            emit(Resource.Success(true))
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading schedules", e)
            emit(Resource.Error("Failed to download schedules: ${e.message}"))
        }
    }

    // Check if schedules need to be refreshed
    fun needsRefresh(): Boolean {
        val lastDownload = sharedPreferences.getLong(KEY_LAST_DOWNLOAD, 0)
        return System.currentTimeMillis() - lastDownload > REFRESH_INTERVAL
    }

    // Get offline data status
    fun getOfflineDataStatus(): Flow<Resource<OfflineDataStatus>> = flow {
        try {
            emit(Resource.Loading())
            val status = flightScheduleRepository.getOfflineDataStatus()
            emitAll(status.map { Resource.Success(it) })
        } catch (e: Exception) {
            emit(Resource.Error("Failed to get offline status: ${e.message}"))
        }
    }

    // Auto-refresh schedules if needed
    suspend fun autoRefreshIfNeeded(): Flow<Resource<Boolean>> = flow {
        if (needsRefresh() && isAutoRefreshEnabled()) {
            emitAll(downloadAndCacheSchedules())
        } else {
            emit(Resource.Success(false))
        }
    }

    // Clear all cached data
    suspend fun clearAllCachedData(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())

            flightScheduleRepository.deleteAllSchedules()
            flightScheduleRepository.deleteAllPackages()

            sharedPreferences.edit().remove(KEY_LAST_DOWNLOAD).apply()

            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to clear cached data: ${e.message}"))
        }
    }

    // Get download statistics
    fun getDownloadStats(): Flow<Resource<DownloadStats>> = flow {
        try {
            emit(Resource.Loading())

            val lastDownload = sharedPreferences.getLong(KEY_LAST_DOWNLOAD, 0)
            val scheduleCount = flightScheduleRepository.getScheduleCount()

            val stats = DownloadStats(
                lastDownloadTime = lastDownload,
                scheduleCount = scheduleCount.first(),
                isAutoRefreshEnabled = isAutoRefreshEnabled(),
                storageSize = calculateStorageSize(),
            )

            emit(Resource.Success(stats))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to get download stats: ${e.message}"))
        }
    }

    // Set auto-refresh preference
    fun setAutoRefreshEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_AUTO_REFRESH_ENABLED, enabled).apply()
    }

    // Check if auto-refresh is enabled
    fun isAutoRefreshEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_AUTO_REFRESH_ENABLED, true)
    }

    // Generate mock flight schedules for demo
    private fun generateMockFlightSchedules(): List<FlightSchedule> {
        return listOf(
            FlightSchedule(
                flightNumber = "CA1234",
                airline = "中国国际航空",
                departureAirport = "北京首都国际机场",
                departureAirportCode = "PEK",
                arrivalAirport = "上海虹桥国际机场",
                arrivalAirportCode = "SHA",
                departureTime = "08:30",
                arrivalTime = "10:45",
                duration = 135,
                daysOfWeek = listOf(1, 2, 3, 4, 5, 6, 7),
                aircraftType = "Boeing 737-800",
                isDomestic = true,
            ),
            FlightSchedule(
                flightNumber = "MU5678",
                airline = "中国东方航空",
                departureAirport = "上海虹桥国际机场",
                departureAirportCode = "SHA",
                arrivalAirport = "深圳宝安国际机场",
                arrivalAirportCode = "SZX",
                departureTime = "14:20",
                arrivalTime = "17:10",
                duration = 170,
                daysOfWeek = listOf(1, 2, 3, 4, 5, 6, 7),
                aircraftType = "Airbus A320",
                isDomestic = true,
            ),
            FlightSchedule(
                flightNumber = "CZ9012",
                airline = "中国南方航空",
                departureAirport = "广州白云国际机场",
                departureAirportCode = "CAN",
                arrivalAirport = "北京首都国际机场",
                arrivalAirportCode = "PEK",
                departureTime = "09:15",
                arrivalTime = "12:00",
                duration = 165,
                daysOfWeek = listOf(1, 3, 5, 7),
                aircraftType = "Boeing 777-300ER",
                isDomestic = true,
            ),
            FlightSchedule(
                flightNumber = "CA1001",
                airline = "中国国际航空",
                departureAirport = "北京首都国际机场",
                departureAirportCode = "PEK",
                arrivalAirport = "洛杉矶国际机场",
                arrivalAirportCode = "LAX",
                departureTime = "13:00",
                arrivalTime = "09:30",
                duration = 750,
                daysOfWeek = listOf(1, 2, 4, 6),
                aircraftType = "Boeing 787-9",
                isDomestic = false,
            ),
            FlightSchedule(
                flightNumber = "UA857",
                airline = "美国联合航空",
                departureAirport = "旧金山国际机场",
                departureAirportCode = "SFO",
                arrivalAirport = "东京成田国际机场",
                arrivalAirportCode = "NRT",
                departureTime = "11:30",
                arrivalTime = "15:45",
                duration = 675,
                daysOfWeek = listOf(2, 4, 6),
                aircraftType = "Boeing 777-200ER",
                isDomestic = false,
            ),
        )
    }

    // Generate mock airlines
    private fun generateMockAirlines(): List<AirlineInfo> {
        return listOf(
            AirlineInfo(
                code = "CA",
                name = "中国国际航空",
                logoUrl = null,
                country = "中国",
            ),
            AirlineInfo(
                code = "MU",
                name = "中国东方航空",
                logoUrl = null,
                country = "中国",
            ),
            AirlineInfo(
                code = "CZ",
                name = "中国南方航空",
                logoUrl = null,
                country = "中国",
            ),
            AirlineInfo(
                code = "UA",
                name = "美国联合航空",
                logoUrl = null,
                country = "美国",
            ),
        )
    }

    // Generate mock airports
    private fun generateMockAirports(): List<OfflineAirportInfo> {
        return listOf(
            OfflineAirportInfo(
                iataCode = "PEK",
                icaoCode = "ZBAA",
                name = "北京首都国际机场",
                city = "北京",
                country = "中国",
                latitude = 40.0799,
                longitude = 116.6031,
                timezone = "Asia/Shanghai",
            ),
            OfflineAirportInfo(
                iataCode = "SHA",
                icaoCode = "ZSSS",
                name = "上海虹桥国际机场",
                city = "上海",
                country = "中国",
                latitude = 31.1979,
                longitude = 121.3362,
                timezone = "Asia/Shanghai",
            ),
            OfflineAirportInfo(
                iataCode = "SZX",
                icaoCode = "ZGSZ",
                name = "深圳宝安国际机场",
                city = "深圳",
                country = "中国",
                latitude = 22.6393,
                longitude = 113.8111,
                timezone = "Asia/Shanghai",
            ),
            OfflineAirportInfo(
                iataCode = "CAN",
                icaoCode = "ZGGG",
                name = "广州白云国际机场",
                city = "广州",
                country = "中国",
                latitude = 23.3924,
                longitude = 113.2988,
                timezone = "Asia/Shanghai",
            ),
            OfflineAirportInfo(
                iataCode = "LAX",
                icaoCode = "KLAX",
                name = "洛杉矶国际机场",
                city = "洛杉矶",
                country = "美国",
                latitude = 33.9425,
                longitude = -118.4081,
                timezone = "America/Los_Angeles",
            ),
            OfflineAirportInfo(
                iataCode = "SFO",
                icaoCode = "KSFO",
                name = "旧金山国际机场",
                city = "旧金山",
                country = "美国",
                latitude = 37.6213,
                longitude = -122.3790,
                timezone = "America/Los_Angeles",
            ),
            OfflineAirportInfo(
                iataCode = "NRT",
                icaoCode = "RJAA",
                name = "东京成田国际机场",
                city = "东京",
                country = "日本",
                latitude = 35.7720,
                longitude = 140.3929,
                timezone = "Asia/Tokyo",
            ),
        )
    }

    // Calculate storage size (mock implementation)
    private fun calculateStorageSize(): Long {
        return 5 * 1024 * 1024L // 5MB mock size
    }
}

data class DownloadStats(
    val lastDownloadTime: Long,
    val scheduleCount: Int,
    val isAutoRefreshEnabled: Boolean,
    val storageSize: Long,
)
