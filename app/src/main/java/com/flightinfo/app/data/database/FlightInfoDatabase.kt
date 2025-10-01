package com.flightinfo.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.flightinfo.app.data.dao.BaggageDao
import com.flightinfo.app.data.dao.BookmarkedFlightDao
import com.flightinfo.app.data.dao.DownloadedSchedulePackageDao
import com.flightinfo.app.data.dao.FamilyAccountDao
import com.flightinfo.app.data.dao.FamilyMemberDao
import com.flightinfo.app.data.dao.FavoriteRouteDao
import com.flightinfo.app.data.dao.FlightScheduleDao
import com.flightinfo.app.data.dao.HistoricalFlightDao
import com.flightinfo.app.data.dao.ItineraryDao
import com.flightinfo.app.data.dao.NotificationHistoryDao
import com.flightinfo.app.data.dao.NotificationPreferencesDao
import com.flightinfo.app.data.dao.OfflineAirportInfoDao
import com.flightinfo.app.data.dao.SharedItineraryDao
import com.flightinfo.app.data.dao.SyncRecordDao
import com.flightinfo.app.data.dao.TrackedFlightDao
import com.flightinfo.app.data.dao.UserBehaviorDao
import com.flightinfo.app.data.dao.UserDao
import com.flightinfo.app.data.model.BaggageItem
import com.flightinfo.app.data.model.BookmarkedFlight
import com.flightinfo.app.data.model.DownloadedSchedulePackage
import com.flightinfo.app.data.model.FamilyAccount
import com.flightinfo.app.data.model.FamilyMember
import com.flightinfo.app.data.model.FavoriteRoute
import com.flightinfo.app.data.model.FlightSchedule
import com.flightinfo.app.data.model.HistoricalFlight
import com.flightinfo.app.data.model.Itinerary
import com.flightinfo.app.data.model.ItineraryConverters
import com.flightinfo.app.data.model.NotificationConverters
import com.flightinfo.app.data.model.NotificationHistory
import com.flightinfo.app.data.model.NotificationPreferences
import com.flightinfo.app.data.model.OfflineAirportInfo
import com.flightinfo.app.data.model.RecommendationCache
import com.flightinfo.app.data.model.RecommendationConverters
import com.flightinfo.app.data.model.ScheduleConverters
import com.flightinfo.app.data.model.SharedItinerary
import com.flightinfo.app.data.model.SyncRecord
import com.flightinfo.app.data.model.SyncRecordConverters
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
        BaggageItem::class,
        SyncRecord::class,
        FamilyAccount::class,
        FamilyMember::class,
        SharedItinerary::class,
        NotificationHistory::class,
        NotificationPreferences::class,
    ],
    version = 12,
    exportSchema = false,
)
@TypeConverters(
    ScheduleConverters::class,
    TrackedFlightConverters::class,
    RecommendationConverters::class,
    ItineraryConverters::class,
    SyncRecordConverters::class,
    NotificationConverters::class,
    Converters::class,
)
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
    abstract fun baggageDao(): BaggageDao
    abstract fun syncRecordDao(): SyncRecordDao
    abstract fun familyAccountDao(): FamilyAccountDao
    abstract fun familyMemberDao(): FamilyMemberDao
    abstract fun sharedItineraryDao(): SharedItineraryDao
    abstract fun notificationHistoryDao(): NotificationHistoryDao
    abstract fun notificationPreferencesDao(): NotificationPreferencesDao

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

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `baggage_items` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `baggage_tag_number` TEXT NOT NULL,
                        `flight_number` TEXT NOT NULL,
                        `baggage_type` TEXT NOT NULL,
                        `weight` REAL NOT NULL,
                        `weight_unit` TEXT NOT NULL,
                        `status` TEXT NOT NULL,
                        `last_location` TEXT,
                        `last_updated` INTEGER NOT NULL,
                        `check_in_time` INTEGER,
                        `loaded_time` INTEGER,
                        `unloaded_time` INTEGER,
                        `carousel_number` TEXT,
                        `special_handling` INTEGER NOT NULL,
                        `notes` TEXT
                    )
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_baggage_items_flight_number` ON `baggage_items` (`flight_number`)
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_baggage_items_baggage_tag_number` ON `baggage_items` (`baggage_tag_number`)
                    """.trimIndent(),
                )
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `sync_records` (
                        `id` TEXT PRIMARY KEY NOT NULL,
                        `dataType` TEXT NOT NULL,
                        `entityId` TEXT NOT NULL,
                        `action` TEXT NOT NULL,
                        `data` TEXT NOT NULL,
                        `metadata` TEXT NOT NULL,
                        `status` TEXT NOT NULL,
                        `retryCount` INTEGER NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `syncedAt` INTEGER
                    )
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_sync_records_status` ON `sync_records` (`status`)
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_sync_records_dataType_entityId` ON `sync_records` (`dataType`, `entityId`)
                    """.trimIndent(),
                )
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `family_accounts` (
                        `id` TEXT PRIMARY KEY NOT NULL,
                        `name` TEXT NOT NULL,
                        `ownerId` TEXT NOT NULL,
                        `inviteCode` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `isActive` INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS `index_family_accounts_inviteCode` ON `family_accounts` (`inviteCode`)
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `family_members` (
                        `id` TEXT PRIMARY KEY NOT NULL,
                        `familyAccountId` TEXT NOT NULL,
                        `userId` TEXT NOT NULL,
                        `userEmail` TEXT NOT NULL,
                        `userName` TEXT NOT NULL,
                        `role` TEXT NOT NULL,
                        `joinedAt` INTEGER NOT NULL,
                        `permissions` TEXT NOT NULL,
                        `isActive` INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_family_members_familyAccountId` ON `family_members` (`familyAccountId`)
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_family_members_userId` ON `family_members` (`userId`)
                    """.trimIndent(),
                )
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `shared_itineraries` (
                        `id` TEXT PRIMARY KEY NOT NULL,
                        `itineraryId` TEXT NOT NULL,
                        `sharedBy` TEXT NOT NULL,
                        `sharedWith` TEXT NOT NULL,
                        `familyAccountId` TEXT,
                        `permissions` TEXT NOT NULL,
                        `sharedAt` INTEGER NOT NULL,
                        `expiresAt` INTEGER,
                        `isActive` INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_shared_itineraries_itineraryId` ON `shared_itineraries` (`itineraryId`)
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_shared_itineraries_sharedWith` ON `shared_itineraries` (`sharedWith`)
                    """.trimIndent(),
                )
                database.execSQL(
                    """
                    CREATE INDEX IF NOT EXISTS `index_shared_itineraries_familyAccountId` ON `shared_itineraries` (`familyAccountId`)
                    """.trimIndent(),
                )
            }
        }

        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create notification_history table
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `notification_history` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `title` TEXT NOT NULL,
                        `message` TEXT NOT NULL,
                        `type` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `flightNumber` TEXT,
                        `departureAirport` TEXT,
                        `arrivalAirport` TEXT,
                        `airline` TEXT,
                        `baggageTagNumber` TEXT,
                        `baggageStatus` TEXT,
                        `timestamp` INTEGER NOT NULL,
                        `scheduledTime` INTEGER,
                        `isRead` INTEGER NOT NULL,
                        `priority` TEXT NOT NULL,
                        `extraData` TEXT
                    )
                    """.trimIndent(),
                )

                // Create notification_preferences table
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `notification_preferences` (
                        `userId` TEXT PRIMARY KEY NOT NULL,
                        `notificationsEnabled` INTEGER NOT NULL,
                        `dndEnabled` INTEGER NOT NULL,
                        `dndStartHour` INTEGER NOT NULL,
                        `dndStartMinute` INTEGER NOT NULL,
                        `dndEndHour` INTEGER NOT NULL,
                        `dndEndMinute` INTEGER NOT NULL,
                        `flightStatusEnabled` INTEGER NOT NULL,
                        `flightDelayEnabled` INTEGER NOT NULL,
                        `flightCancellationEnabled` INTEGER NOT NULL,
                        `boardingTimeEnabled` INTEGER NOT NULL,
                        `gateChangeEnabled` INTEGER NOT NULL,
                        `baggageStatusEnabled` INTEGER NOT NULL,
                        `priceAlertEnabled` INTEGER NOT NULL,
                        `tripReminderEnabled` INTEGER NOT NULL,
                        `weatherAlertEnabled` INTEGER NOT NULL,
                        `watchedFlightNumbers` TEXT NOT NULL,
                        `watchedAirports` TEXT NOT NULL,
                        `watchedAirlines` TEXT NOT NULL,
                        `notificationFrequency` TEXT NOT NULL,
                        `vibrationEnabled` INTEGER NOT NULL,
                        `soundEnabled` INTEGER NOT NULL,
                        `ledEnabled` INTEGER NOT NULL,
                        `scheduledNotifications` TEXT NOT NULL,
                        `lastUpdated` INTEGER NOT NULL
                    )
                    """.trimIndent(),
                )

                // Insert default preferences row if absent
                database.execSQL(
                    """
                    INSERT OR IGNORE INTO `notification_preferences` (userId, notificationsEnabled, dndEnabled, dndStartHour, dndStartMinute, dndEndHour, dndEndMinute, 
                        flightStatusEnabled, flightDelayEnabled, flightCancellationEnabled, boardingTimeEnabled, gateChangeEnabled, baggageStatusEnabled, priceAlertEnabled, 
                        tripReminderEnabled, weatherAlertEnabled, watchedFlightNumbers, watchedAirports, watchedAirlines, notificationFrequency, vibrationEnabled, soundEnabled, ledEnabled, scheduledNotifications, lastUpdated)
                    VALUES ('default_user', 1, 0, 22, 0, 7, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, '[]', '[]', '[]', 'IMMEDIATE', 1, 1, 1, '[]', strftime('%s','now')*1000)
                    """.trimIndent(),
                )
            }
        }
    }
}
