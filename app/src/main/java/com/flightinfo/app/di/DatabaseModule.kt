package com.flightinfo.app.di

import android.content.Context
import androidx.room.Room
import com.flightinfo.app.data.dao.BaggageDao
import com.flightinfo.app.data.dao.BookmarkedFlightDao
import com.flightinfo.app.data.dao.DownloadedSchedulePackageDao
import com.flightinfo.app.data.dao.FavoriteRouteDao
import com.flightinfo.app.data.dao.FlightScheduleDao
import com.flightinfo.app.data.dao.HistoricalFlightDao
import com.flightinfo.app.data.dao.ItineraryDao
import com.flightinfo.app.data.dao.NotificationHistoryDao
import com.flightinfo.app.data.dao.NotificationPreferencesDao
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
        )
            .addMigrations(
                FlightInfoDatabase.MIGRATION_3_4,
                FlightInfoDatabase.MIGRATION_4_5,
                FlightInfoDatabase.MIGRATION_5_6,
                FlightInfoDatabase.MIGRATION_6_7,
                FlightInfoDatabase.MIGRATION_7_8,
                FlightInfoDatabase.MIGRATION_8_9,
                FlightInfoDatabase.MIGRATION_9_10,
                FlightInfoDatabase.MIGRATION_10_11,
                FlightInfoDatabase.MIGRATION_11_12,
            )
            // 数据库优化配置
            .setQueryExecutor {
                // 使用自定义线程池处理数据库查询
                Thread {
                    it.run()
                }.start()
            }
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
    fun provideItineraryDao(database: FlightInfoDatabase): ItineraryDao {
        return database.itineraryDao()
    }

    @Provides
    fun provideBaggageDao(database: FlightInfoDatabase): BaggageDao {
        return database.baggageDao()
    }

    @Provides
    fun provideNotificationHistoryDao(database: FlightInfoDatabase): NotificationHistoryDao {
        return database.notificationHistoryDao()
    }

    @Provides
    fun provideNotificationPreferencesDao(database: FlightInfoDatabase): NotificationPreferencesDao {
        return database.notificationPreferencesDao()
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}
