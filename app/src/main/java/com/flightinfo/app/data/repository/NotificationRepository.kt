package com.flightinfo.app.data.repository

import com.flightinfo.app.data.dao.NotificationHistoryDao
import com.flightinfo.app.data.dao.NotificationPreferencesDao
import com.flightinfo.app.data.model.NotificationCategory
import com.flightinfo.app.data.model.NotificationHistory
import com.flightinfo.app.data.model.NotificationPreferences
import com.flightinfo.app.data.model.NotificationType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 通知仓库类
 * 负责管理通知历史和偏好设置的数据操作
 */
@Singleton
class NotificationRepository @Inject constructor(
    private val notificationHistoryDao: NotificationHistoryDao,
    private val notificationPreferencesDao: NotificationPreferencesDao,
) {

    // 通知历史相关方法
    suspend fun insertNotification(notification: NotificationHistory): Long {
        return notificationHistoryDao.insertNotification(notification)
    }

    fun getAllNotifications(): Flow<List<NotificationHistory>> {
        return notificationHistoryDao.getAllNotifications()
    }

    fun getUnreadNotifications(): Flow<List<NotificationHistory>> {
        return notificationHistoryDao.getUnreadNotifications()
    }

    fun getNotificationsByType(type: NotificationType): Flow<List<NotificationHistory>> {
        return notificationHistoryDao.getNotificationsByType(type)
    }

    fun getNotificationsByCategory(category: NotificationCategory): Flow<List<NotificationHistory>> {
        return notificationHistoryDao.getNotificationsByCategory(category)
    }

    fun getNotificationsByFlight(flightNumber: String): Flow<List<NotificationHistory>> {
        return notificationHistoryDao.getNotificationsByFlight(flightNumber)
    }

    fun getNotificationsByAirport(airport: String): Flow<List<NotificationHistory>> {
        return notificationHistoryDao.getNotificationsByAirport(airport)
    }

    fun getUnreadCount(): Flow<Int> {
        return notificationHistoryDao.getUnreadCount()
    }

    suspend fun markAsRead(id: Long) {
        notificationHistoryDao.markAsRead(id)
    }

    suspend fun markAllAsRead() {
        notificationHistoryDao.markAllAsRead()
    }

    suspend fun deleteNotification(id: Long) {
        notificationHistoryDao.deleteNotificationById(id)
    }

    suspend fun deleteAllNotifications() {
        notificationHistoryDao.deleteAllNotifications()
    }

    // 通知偏好设置相关方法
    fun getNotificationPreferences(): Flow<NotificationPreferences?> {
        return notificationPreferencesDao.getPreferences()
    }

    suspend fun getNotificationPreferencesSync(): NotificationPreferences? {
        return notificationPreferencesDao.getPreferencesSync()
    }

    suspend fun updateNotificationPreferences(preferences: NotificationPreferences) {
        notificationPreferencesDao.updatePreferences(preferences)
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        notificationPreferencesDao.setNotificationsEnabled(enabled = enabled)
    }

    suspend fun updateDndSettings(
        enabled: Boolean,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
    ) {
        notificationPreferencesDao.updateDndSettings(
            enabled = enabled,
            startHour = startHour,
            startMinute = startMinute,
            endHour = endHour,
            endMinute = endMinute,
        )
    }

    suspend fun setFlightStatusEnabled(enabled: Boolean) {
        notificationPreferencesDao.setFlightStatusEnabled(enabled = enabled)
    }

    suspend fun setFlightDelayEnabled(enabled: Boolean) {
        notificationPreferencesDao.setFlightDelayEnabled(enabled = enabled)
    }

    suspend fun setFlightCancellationEnabled(enabled: Boolean) {
        notificationPreferencesDao.setFlightCancellationEnabled(enabled = enabled)
    }

    suspend fun setBoardingTimeEnabled(enabled: Boolean) {
        notificationPreferencesDao.setBoardingTimeEnabled(enabled = enabled)
    }

    suspend fun setGateChangeEnabled(enabled: Boolean) {
        notificationPreferencesDao.setGateChangeEnabled(enabled = enabled)
    }

    suspend fun setBaggageStatusEnabled(enabled: Boolean) {
        notificationPreferencesDao.setBaggageStatusEnabled(enabled = enabled)
    }

    suspend fun setPriceAlertEnabled(enabled: Boolean) {
        notificationPreferencesDao.setPriceAlertEnabled(enabled = enabled)
    }

    suspend fun setTripReminderEnabled(enabled: Boolean) {
        notificationPreferencesDao.setTripReminderEnabled(enabled = enabled)
    }

    suspend fun setWeatherAlertEnabled(enabled: Boolean) {
        notificationPreferencesDao.setWeatherAlertEnabled(enabled = enabled)
    }

    suspend fun updateWatchedFlights(flightNumbers: List<String>) {
        notificationPreferencesDao.updateWatchedFlights(flightNumbers = flightNumbers)
    }

    suspend fun updateWatchedAirports(airports: List<String>) {
        notificationPreferencesDao.updateWatchedAirports(airports = airports)
    }

    suspend fun updateWatchedAirlines(airlines: List<String>) {
        notificationPreferencesDao.updateWatchedAirlines(airlines = airlines)
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        notificationPreferencesDao.setVibrationEnabled(enabled = enabled)
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        notificationPreferencesDao.setSoundEnabled(enabled = enabled)
    }

    suspend fun setLedEnabled(enabled: Boolean) {
        notificationPreferencesDao.setLedEnabled(enabled = enabled)
    }

    suspend fun initializeDefaultPreferences() {
        notificationPreferencesDao.insertDefaultPreferences()
    }
}
