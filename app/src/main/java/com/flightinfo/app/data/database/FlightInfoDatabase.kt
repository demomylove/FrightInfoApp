package com.flightinfo.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flightinfo.app.data.dao.TrackedFlightDao
import com.flightinfo.app.data.model.TrackedFlight

@Database(
    entities = [TrackedFlight::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class FlightInfoDatabase : RoomDatabase() {
    abstract fun trackedFlightDao(): TrackedFlightDao
}
