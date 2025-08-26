package com.flightinfo.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flightinfo.app.data.dao.BookmarkedFlightDao
import com.flightinfo.app.data.dao.DownloadedSchedulePackageDao
import com.flightinfo.app.data.dao.FlightScheduleDao
import com.flightinfo.app.data.dao.OfflineAirportInfoDao
import com.flightinfo.app.data.dao.TrackedFlightDao
import com.flightinfo.app.data.model.AirlineInfo
import com.flightinfo.app.data.model.BookmarkedFlight
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
        OfflineAirportInfo::class,
        DownloadedSchedulePackage::class,
        BookmarkedFlight::class,
    ],
    version = 3,
    exportSchema = false,
)
@TypeConverters(ScheduleConverters::class, TrackedFlightConverters::class)
abstract class FlightInfoDatabase : RoomDatabase() {
    abstract fun trackedFlightDao(): TrackedFlightDao
    abstract fun flightScheduleDao(): FlightScheduleDao
    abstract fun offlineAirportInfoDao(): OfflineAirportInfoDao
    abstract fun downloadedSchedulePackageDao(): DownloadedSchedulePackageDao
    abstract fun bookmarkedFlightDao(): BookmarkedFlightDao
}
