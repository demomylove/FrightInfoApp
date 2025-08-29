package com.flightinfo.app.di

import android.content.Context
import androidx.room.Room
import com.flightinfo.app.data.dao.BookmarkedFlightDao
import com.flightinfo.app.data.dao.DownloadedSchedulePackageDao
import com.flightinfo.app.data.dao.FavoriteRouteDao
import com.flightinfo.app.data.dao.FlightScheduleDao
import com.flightinfo.app.data.dao.HistoricalFlightDao
import com.flightinfo.app.data.dao.OfflineAirportInfoDao
import com.flightinfo.app.data.dao.TrackedFlightDao
import com.flightinfo.app.data.dao.UserBehaviorDao
import com.flightinfo.app.data.dao.UserDao
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
        ).addMigrations(FlightInfoDatabase.MIGRATION_4_5, FlightInfoDatabase.MIGRATION_5_6)
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
    fun provideOfflineAirportInfoDao(database: FlightInfoDatabase): OfflineAirportInfoDao {
        return database.offlineAirportInfoDao()
    }

    @Provides
    fun provideDownloadedSchedulePackageDao(database: FlightInfoDatabase): DownloadedSchedulePackageDao {
        return database.downloadedSchedulePackageDao()
    }

    @Provides
    fun provideBookmarkedFlightDao(database: FlightInfoDatabase): BookmarkedFlightDao {
        return database.bookmarkedFlightDao()
    }

    @Provides
    fun provideUserBehaviorDao(database: FlightInfoDatabase): UserBehaviorDao {
        return database.userBehaviorDao()
    }

    @Provides
    fun provideFavoriteRouteDao(database: FlightInfoDatabase): FavoriteRouteDao {
        return database.favoriteRouteDao()
    }

    @Provides
    fun provideHistoricalFlightDao(database: FlightInfoDatabase): HistoricalFlightDao {
        return database.historicalFlightDao()
    }

    @Provides
    fun provideUserDao(database: FlightInfoDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}
