package com.flightinfo.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.flightinfo.app.data.model.NotificationPreferences
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationPreferencesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreferences(preferences: NotificationPreferences)

    @Update
    suspend fun updatePreferences(preferences: NotificationPreferences)

    @Delete
    suspend fun deletePreferences(preferences: NotificationPreferences)

    @Query("SELECT * FROM notification_preferences WHERE userId = :userId")
    fun getPreferences(userId: String = "default_user"): Flow<NotificationPreferences?>

    @Query("SELECT * FROM notification_preferences WHERE userId = :userId")
    suspend fun getPreferencesSync(userId: String = "default_user"): NotificationPreferences?

    @Query("UPDATE notification_preferences SET notificationsEnabled = :enabled WHERE userId = :userId")
    suspend fun setNotificationsEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("UPDATE notification_preferences SET dndEnabled = :enabled, dndStartHour = :startHour, dndStartMinute = :startMinute, dndEndHour = :endHour, dndEndMinute = :endMinute WHERE userId = :userId")
    suspend fun updateDndSettings(
        userId: String = "default_user",
        enabled: Boolean,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
    )

    @Query("UPDATE notification_preferences SET flightStatusEnabled = :enabled WHERE userId = :userId")
    suspend fun setFlightStatusEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("UPDATE notification_preferences SET flightDelayEnabled = :enabled WHERE userId = :userId")
    suspend fun setFlightDelayEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("UPDATE notification_preferences SET flightCancellationEnabled = :enabled WHERE userId = :userId")
    suspend fun setFlightCancellationEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("UPDATE notification_preferences SET boardingTimeEnabled = :enabled WHERE userId = :userId")
    suspend fun setBoardingTimeEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("UPDATE notification_preferences SET gateChangeEnabled = :enabled WHERE userId = :userId")
    suspend fun setGateChangeEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("UPDATE notification_preferences SET baggageStatusEnabled = :enabled WHERE userId = :userId")
    suspend fun setBaggageStatusEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("UPDATE notification_preferences SET priceAlertEnabled = :enabled WHERE userId = :userId")
    suspend fun setPriceAlertEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("UPDATE notification_preferences SET tripReminderEnabled = :enabled WHERE userId = :userId")
    suspend fun setTripReminderEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("UPDATE notification_preferences SET weatherAlertEnabled = :enabled WHERE userId = :userId")
    suspend fun setWeatherAlertEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("UPDATE notification_preferences SET watchedFlightNumbers = :flightNumbers WHERE userId = :userId")
    suspend fun updateWatchedFlights(userId: String = "default_user", flightNumbers: List<String>)

    @Query("UPDATE notification_preferences SET watchedAirports = :airports WHERE userId = :userId")
    suspend fun updateWatchedAirports(userId: String = "default_user", airports: List<String>)

    @Query("UPDATE notification_preferences SET watchedAirlines = :airlines WHERE userId = :userId")
    suspend fun updateWatchedAirlines(userId: String = "default_user", airlines: List<String>)

    @Query("UPDATE notification_preferences SET vibrationEnabled = :enabled WHERE userId = :userId")
    suspend fun setVibrationEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("UPDATE notification_preferences SET soundEnabled = :enabled WHERE userId = :userId")
    suspend fun setSoundEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("UPDATE notification_preferences SET ledEnabled = :enabled WHERE userId = :userId")
    suspend fun setLedEnabled(userId: String = "default_user", enabled: Boolean)

    @Query("INSERT OR IGNORE INTO notification_preferences (userId) VALUES (:userId)")
    suspend fun insertDefaultPreferences(userId: String = "default_user")
}
