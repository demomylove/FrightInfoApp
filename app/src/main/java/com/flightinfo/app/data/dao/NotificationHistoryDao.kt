package com.flightinfo.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.flightinfo.app.data.model.NotificationCategory
import com.flightinfo.app.data.model.NotificationHistory
import com.flightinfo.app.data.model.NotificationType
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationHistory): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationHistory>)

    @Update
    suspend fun updateNotification(notification: NotificationHistory)

    @Delete
    suspend fun deleteNotification(notification: NotificationHistory)

    @Query("DELETE FROM notification_history WHERE id = :id")
    suspend fun deleteNotificationById(id: Long)

    @Query("DELETE FROM notification_history")
    suspend fun deleteAllNotifications()

    @Query("SELECT * FROM notification_history WHERE id = :id")
    suspend fun getNotificationById(id: Long): NotificationHistory?

    @Query("SELECT * FROM notification_history ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationHistory>>

    @Query("SELECT * FROM notification_history WHERE isRead = 0 ORDER BY timestamp DESC")
    fun getUnreadNotifications(): Flow<List<NotificationHistory>>

    @Query("SELECT * FROM notification_history WHERE type = :type ORDER BY timestamp DESC")
    fun getNotificationsByType(type: NotificationType): Flow<List<NotificationHistory>>

    @Query("SELECT * FROM notification_history WHERE category = :category ORDER BY timestamp DESC")
    fun getNotificationsByCategory(category: NotificationCategory): Flow<List<NotificationHistory>>

    @Query("SELECT * FROM notification_history WHERE flightNumber = :flightNumber ORDER BY timestamp DESC")
    fun getNotificationsByFlight(flightNumber: String): Flow<List<NotificationHistory>>

    @Query("SELECT * FROM notification_history WHERE departureAirport = :airport OR arrivalAirport = :airport ORDER BY timestamp DESC")
    fun getNotificationsByAirport(airport: String): Flow<List<NotificationHistory>>

    @Query("SELECT COUNT(*) FROM notification_history WHERE isRead = 0")
    fun getUnreadCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM notification_history WHERE type = :type AND isRead = 0")
    fun getUnreadCountByType(type: NotificationType): Flow<Int>

    @Query("UPDATE notification_history SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE notification_history SET isRead = 1 WHERE isRead = 0")
    suspend fun markAllAsRead()

    @Query("UPDATE notification_history SET isRead = 1 WHERE type = :type")
    suspend fun markAllAsReadByType(type: NotificationType)

    @Query("DELETE FROM notification_history WHERE timestamp < :timestamp")
    suspend fun deleteOldNotifications(timestamp: Long)

    @Query("SELECT COUNT(*) FROM notification_history WHERE timestamp BETWEEN :startTime AND :endTime")
    suspend fun getNotificationCountBetween(startTime: Long, endTime: Long): Int

    @Query("SELECT * FROM notification_history WHERE timestamp >= :since ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentNotifications(since: Long, limit: Int = 50): Flow<List<NotificationHistory>>

    @Query("SELECT * FROM notification_history ORDER BY timestamp DESC LIMIT :limit")
    fun getLatestNotifications(limit: Int = 20): Flow<List<NotificationHistory>>
}
