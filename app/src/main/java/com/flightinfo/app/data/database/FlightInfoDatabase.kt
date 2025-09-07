package com.flightinfo.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.flightinfo.app.data.dao.BookmarkedFlightDao
import com.flightinfo.app.data.dao.DownloadedSchedulePackageDao
import com.flightinfo.app.data.dao.FavoriteRouteDao
import com.flightinfo.app.data.dao.FlightScheduleDao
import com.flightinfo.app.data.dao.HistoricalFlightDao
import com.flightinfo.app.data.dao.ItineraryDao
import com.flightinfo.app.data.dao.OfflineAirportInfoDao
import com.flightinfo.app.data.dao.TrackedFlightDao
import com.flightinfo.app.data.dao.UserBehaviorDao
import com.flightinfo.app.data.dao.UserDao
import com.flightinfo.app.data.model.BookmarkedFlight
import com.flightinfo.app.data.model.DownloadedSchedulePackage
import com.flightinfo.app.data.model.FavoriteRoute
import com.flightinfo.app.data.model.FlightSchedule
import com.flightinfo.app.data.model.HistoricalFlight
import com.flightinfo.app.data.model.Itinerary
import com.flightinfo.app.data.model.ItineraryConverters
import com.flightinfo.app.data.model.OfflineAirportInfo
import com.flightinfo.app.data.model.RecommendationCache
import com.flightinfo.app.data.model.RecommendationConverters
import com.flightinfo.app.data.model.ScheduleConverters
import com.flightinfo.app.data.model.TrackedFlight
import com.flightinfo.app.data.model.TrackedFlightConverters
import com.flightinfo.app.data.model.User
import com.flightinfo.app.data.model.UserBehavior
import com.flightinfo.app.data.model.UserPreferences

@Database(
    entities = [
        TrackedFlight::class,
        FlightSchedule::class,
        OfflineAirportInfo::class,
        DownloadedSchedulePackage::class,
        BookmarkedFlight::class,
        UserBehavior::class,
        UserPreferences::class,
        RecommendationCache::class,
        FavoriteRoute::class,
        HistoricalFlight::class,
        User::class,
        Itinerary::class,
    ],
    version = 7,
    exportSchema = false,
)
@TypeConverters(ScheduleConverters::class, TrackedFlightConverters::class, RecommendationConverters::class, ItineraryConverters::class)
abstract class FlightInfoDatabase : RoomDatabase() {
    abstract fun trackedFlightDao(): TrackedFlightDao
    abstract fun flightScheduleDao(): FlightScheduleDao
    abstract fun offlineAirportInfoDao(): OfflineAirportInfoDao
    abstract fun downloadedSchedulePackageDao(): DownloadedSchedulePackageDao
    abstract fun bookmarkedFlightDao(): BookmarkedFlightDao
    abstract fun userBehaviorDao(): UserBehaviorDao
    abstract fun favoriteRouteDao(): FavoriteRouteDao
    abstract fun historicalFlightDao(): HistoricalFlightDao
    abstract fun userDao(): UserDao
    abstract fun itineraryDao(): ItineraryDao

    companion object {
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 创建用户行为表
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS user_behavior (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        actionType TEXT NOT NULL,
                        flightNumber TEXT,
                        departureAirport TEXT,
                        arrivalAirport TEXT,
                        airline TEXT,
                        priceRange TEXT,
                        timestamp INTEGER NOT NULL,
                        metadata TEXT
                    )
                    """.trimIndent(),
                )

                // 创建用户偏好表
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS user_preferences (
                        userId TEXT PRIMARY KEY NOT NULL,
                        favoriteAirlines TEXT NOT NULL,
                        favoriteAirports TEXT NOT NULL,
                        preferredPriceRange TEXT,
                        preferredAirlines TEXT NOT NULL,
                        travelFrequency INTEGER NOT NULL,
                        lastUpdated INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )

                // 创建推荐缓存表
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS recommendation_cache (
                        cacheKey TEXT PRIMARY KEY NOT NULL,
                        recommendations TEXT NOT NULL,
                        timestamp INTEGER NOT NULL,
                        ttl INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )

                // 插入默认用户偏好
                database.execSQL(
                    """
                    INSERT OR REPLACE INTO user_preferences (
                        userId, favoriteAirlines, favoriteAirports, preferredPriceRange, 
                        preferredAirlines, travelFrequency, lastUpdated
                    ) VALUES (
                        'default_user', '[]', '[]', NULL, '[]', 0, ${System.currentTimeMillis()}
                    )
                    """.trimIndent(),
                )
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `favorite_routes` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `originAirport` TEXT NOT NULL,
                        `destinationAirport` TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS `index_favorite_routes_originAirport_destinationAirport` ON `favorite_routes` (`originAirport`, `destinationAirport`)
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `historical_flights` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `flightNumber` TEXT NOT NULL,
                        `airline` TEXT NOT NULL,
                        `departureAirport` TEXT NOT NULL,
                        `arrivalAirport` TEXT NOT NULL,
                        `departureTime` TEXT NOT NULL,
                        `arrivalTime` TEXT NOT NULL,
                        `status` TEXT NOT NULL,
                        `accessedTimestamp` INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `users` (
                        `userId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `email` TEXT NOT NULL,
                        `passwordHash` TEXT NOT NULL
                    )
                    """.trimIndent(),
                )
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `itineraries` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `title` TEXT NOT NULL,
                        `pnr` TEXT,
                        `passengers` TEXT NOT NULL,
                        `startTime` INTEGER NOT NULL,
                        `endTime` INTEGER NOT NULL,
                        `segments` TEXT NOT NULL,
                        `tasks` TEXT NOT NULL,
                        `checkInUrl` TEXT,
                        `boardingPassUrl` TEXT
                    )
                    """.trimIndent(),
                )
            }
        }
    }
}
