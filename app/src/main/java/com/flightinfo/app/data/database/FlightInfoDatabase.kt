package com.flightinfo.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flightinfo.app.data.dao.AirlineInfoDao
import com.flightinfo.app.data.dao.DownloadedSchedulePackageDao
import com.flightinfo.app.data.dao.FlightScheduleDao
import com.flightinfo.app.data.dao.OfflineAirportInfoDao
import com.flightinfo.app.data.dao.TrackedFlightDao
import com.flightinfo.app.data.model.AirlineInfo
import com.flightinfo.app.data.model.DownloadedSchedulePackage
import com.flightinfo.app.data.model.FlightSchedule
import com.flightinfo.app.data.model.OfflineAirportInfo
import com.flightinfo.app.data.model.ScheduleConverters
import com.flightinfo.app.data.model.TrackedFlight
import com.flightinfo.app.data.model.TrackedFlightConverters

@Database(
    entities = [
        TrackedFlight::class,
        FlightSchedule::class,
        AirlineInfo::class,
        OfflineAirportInfo::class,
        DownloadedSchedulePackage::class,
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(ScheduleConverters::class, TrackedFlightConverters::class)
abstract class FlightInfoDatabase : RoomDatabase() {
    abstract fun trackedFlightDao(): TrackedFlightDao
    abstract fun flightScheduleDao(): FlightScheduleDao
    abstract fun airlineInfoDao(): AirlineInfoDao
    abstract fun offlineAirportInfoDao(): OfflineAirportInfoDao
    abstract fun downloadedSchedulePackageDao(): DownloadedSchedulePackageDao
}
