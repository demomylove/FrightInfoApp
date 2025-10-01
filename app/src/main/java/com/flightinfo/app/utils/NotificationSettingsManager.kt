package com.flightinfo.app.utils

import com.flightinfo.app.data.model.NotificationPreferences
import com.flightinfo.app.data.model.NotificationType
import com.flightinfo.app.data.model.ScheduledNotification
import com.flightinfo.app.data.repository.NotificationRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 通知设置管理器
 * 负责管理用户的通知偏好设置和个性化配置
 */
@Singleton
class NotificationSettingsManager @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val enhancedNotificationManager: EnhancedNotificationManager,
) {

    /**
     * 获取当前通知偏好设置
     */
    suspend fun getNotificationPreferences(): NotificationPreferences {
        return notificationRepository.getNotificationPreferencesSync()
            ?: initializeDefaultPreferences()
    }

    /**
     * 初始化默认通知偏好设置
     */
    suspend fun initializeDefaultPreferences(): NotificationPreferences {
        notificationRepository.initializeDefaultPreferences()

        // 等待数据库操作完成
        return notificationRepository.getNotificationPreferences().firstOrNull()
            ?: NotificationPreferences()
    }

    /**
     * 更新通知总开关
     */
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        notificationRepository.setNotificationsEnabled(enabled)
    }

    /**
     * 更新勿扰模式设置
     */
    suspend fun updateDndSettings(
        enabled: Boolean,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
    ) {
        notificationRepository.updateDndSettings(
            enabled = enabled,
            startHour = startHour,
            startMinute = startMinute,
            endHour = endHour,
            endMinute = endMinute,
        )
    }

    /**
     * 更新航班状态通知开关
     */
    suspend fun setFlightStatusEnabled(enabled: Boolean) {
        notificationRepository.setFlightStatusEnabled(enabled)
    }

    /**
     * 更新航班延误通知开关
     */
    suspend fun setFlightDelayEnabled(enabled: Boolean) {
        notificationRepository.setFlightDelayEnabled(enabled)
    }

    /**
     * 更新航班取消通知开关
     */
    suspend fun setFlightCancellationEnabled(enabled: Boolean) {
        notificationRepository.setFlightCancellationEnabled(enabled)
    }

    /**
     * 更新登机时间通知开关
     */
    suspend fun setBoardingTimeEnabled(enabled: Boolean) {
        notificationRepository.setBoardingTimeEnabled(enabled)
    }

    /**
     * 更新登机口变更通知开关
     */
    suspend fun setGateChangeEnabled(enabled: Boolean) {
        notificationRepository.setGateChangeEnabled(enabled)
    }

    /**
     * 更新行李状态通知开关
     */
    suspend fun setBaggageStatusEnabled(enabled: Boolean) {
        notificationRepository.setBaggageStatusEnabled(enabled)
    }

    /**
     * 更新价格提醒通知开关
     */
    suspend fun setPriceAlertEnabled(enabled: Boolean) {
        notificationRepository.setPriceAlertEnabled(enabled)
    }

    /**
     * 更新行程提醒通知开关
     */
    suspend fun setTripReminderEnabled(enabled: Boolean) {
        notificationRepository.setTripReminderEnabled(enabled)
    }

    /**
     * 更新天气提醒通知开关
     */
    suspend fun setWeatherAlertEnabled(enabled: Boolean) {
        notificationRepository.setWeatherAlertEnabled(enabled)
    }

    /**
     * 更新关注的航班号列表
     */
    suspend fun updateWatchedFlights(flightNumbers: List<String>) {
        notificationRepository.updateWatchedFlights(flightNumbers)
    }

    /**
     * 更新关注的机场列表
     */
    suspend fun updateWatchedAirports(airports: List<String>) {
        notificationRepository.updateWatchedAirports(airports)
    }

    /**
     * 更新关注的航空公司列表
     */
    suspend fun updateWatchedAirlines(airlines: List<String>) {
        notificationRepository.updateWatchedAirlines(airlines)
    }

    /**
     * 更新振动设置
     */
    suspend fun setVibrationEnabled(enabled: Boolean) {
        notificationRepository.setVibrationEnabled(enabled)
    }

    /**
     * 更新声音设置
     */
    suspend fun setSoundEnabled(enabled: Boolean) {
        notificationRepository.setSoundEnabled(enabled)
    }

    /**
     * 更新LED灯设置
     */
    suspend fun setLedEnabled(enabled: Boolean) {
        notificationRepository.setLedEnabled(enabled)
    }

    /**
     * 添加关注的航班
     */
    suspend fun addWatchedFlight(flightNumber: String) {
        val currentPrefs = getNotificationPreferences()
        val updatedFlights = (currentPrefs.watchedFlightNumbers + flightNumber)
            .distinct()
            .take(50) // 限制最多50个关注的航班

        notificationRepository.updateWatchedFlights(updatedFlights)
    }

    /**
     * 移除关注的航班
     */
    suspend fun removeWatchedFlight(flightNumber: String) {
        val currentPrefs = getNotificationPreferences()
        val updatedFlights = currentPrefs.watchedFlightNumbers.filter { it != flightNumber }

        notificationRepository.updateWatchedFlights(updatedFlights)
    }

    /**
     * 添加关注的机场
     */
    suspend fun addWatchedAirport(airport: String) {
        val currentPrefs = getNotificationPreferences()
        val updatedAirports = (currentPrefs.watchedAirports + airport)
            .distinct()
            .take(20) // 限制最多20个关注的机场

        notificationRepository.updateWatchedAirports(updatedAirports)
    }

    /**
     * 移除关注的机场
     */
    suspend fun removeWatchedAirport(airport: String) {
        val currentPrefs = getNotificationPreferences()
        val updatedAirports = currentPrefs.watchedAirports.filter { it != airport }

        notificationRepository.updateWatchedAirports(updatedAirports)
    }

    /**
     * 添加关注的航空公司
     */
    suspend fun addWatchedAirline(airline: String) {
        val currentPrefs = getNotificationPreferences()
        val updatedAirlines = (currentPrefs.watchedAirlines + airline)
            .distinct()
            .take(20) // 限制最多20个关注的航空公司

        notificationRepository.updateWatchedAirlines(updatedAirlines)
    }

    /**
     * 移除关注的航空公司
     */
    suspend fun removeWatchedAirline(airline: String) {
        val currentPrefs = getNotificationPreferences()
        val updatedAirlines = currentPrefs.watchedAirlines.filter { it != airline }

        notificationRepository.updateWatchedAirlines(updatedAirlines)
    }

    /**
     * 检查航班是否在关注列表中
     */
    suspend fun isFlightWatched(flightNumber: String): Boolean {
        val preferences = getNotificationPreferences()
        return preferences.watchedFlightNumbers.contains(flightNumber)
    }

    /**
     * 检查机场是否在关注列表中
     */
    suspend fun isAirportWatched(airport: String): Boolean {
        val preferences = getNotificationPreferences()
        return preferences.watchedAirports.contains(airport)
    }

    /**
     * 检查航空公司是否在关注列表中
     */
    suspend fun isAirlineWatched(airline: String): Boolean {
        val preferences = getNotificationPreferences()
        return preferences.watchedAirlines.contains(airline)
    }

    /**
     * 安排定时通知
     */
    fun scheduleNotification(
        title: String,
        message: String,
        type: com.flightinfo.app.data.model.NotificationType,
        scheduledTime: Long,
        repeatInterval: Long? = null,
    ) {
        val scheduledNotification = ScheduledNotification(
            id = UUID.randomUUID().toString(),
            title = title,
            message = message,
            type = type,
            scheduledTime = scheduledTime,
            repeatInterval = repeatInterval,
        )

        enhancedNotificationManager.scheduleNotification(scheduledNotification)
    }

    /**
     * 安排相对时间通知（例如：登机前2小时提醒）
     */
    fun scheduleRelativeNotification(
        title: String,
        message: String,
        type: com.flightinfo.app.data.model.NotificationType,
        delayInMinutes: Long,
    ) {
        val scheduledTime = System.currentTimeMillis() + (delayInMinutes * 60 * 1000)
        scheduleNotification(title, message, type, scheduledTime)
    }

    /**
     * 获取关注的航班列表
     */
    suspend fun getWatchedFlights(): List<String> {
        return getNotificationPreferences().watchedFlightNumbers
    }

    /**
     * 获取关注的机场列表
     */
    suspend fun getWatchedAirports(): List<String> {
        return getNotificationPreferences().watchedAirports
    }

    /**
     * 获取关注的航空公司列表
     */
    suspend fun getWatchedAirlines(): List<String> {
        return getNotificationPreferences().watchedAirlines
    }

    /**
     * 清除所有关注列表
     */
    suspend fun clearAllWatchedItems() {
        notificationRepository.updateWatchedFlights(emptyList())
        notificationRepository.updateWatchedAirports(emptyList())
        notificationRepository.updateWatchedAirlines(emptyList())
    }
}
