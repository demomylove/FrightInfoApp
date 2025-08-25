package com.flightinfo.app.di

import android.content.Context
import androidx.room.Room
import com.flightinfo.app.data.dao.AirlineInfoDao
import com.flightinfo.app.data.dao.DownloadedSchedulePackageDao
import com.flightinfo.app.data.dao.FlightScheduleDao
import com.flightinfo.app.data.dao.OfflineAirportInfoDao
import com.flightinfo.app.data.dao.TrackedFlightDao
import com.flightinfo.app.data.database.FlightInfoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideFlightInfoDatabase(@ApplicationContext context: Context): FlightInfoDatabase {
        return Room.databaseBuilder(
            context,
            FlightInfoDatabase::class.java,
            "flight_info_database",
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTrackedFlightDao(database: FlightInfoDatabase): TrackedFlightDao {
        return database.trackedFlightDao()
    }

    @Provides
    fun provideFlightScheduleDao(database: FlightInfoDatabase): FlightScheduleDao {
        return database.flightScheduleDao()
    }

    @Provides
    fun provideAirlineInfoDao(database: FlightInfoDatabase): AirlineInfoDao {
        return database.airlineInfoDao()
    }

    @Provides
    fun provideOfflineAirportInfoDao(database: FlightInfoDatabase): OfflineAirportInfoDao {
        return database.offlineAirportInfoDao()
    }

    @Provides
    fun provideDownloadedSchedulePackageDao(database: FlightInfoDatabase): DownloadedSchedulePackageDao {
        return database.downloadedSchedulePackageDao()
    }
}
