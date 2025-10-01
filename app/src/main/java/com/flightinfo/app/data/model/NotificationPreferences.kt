package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.flightinfo.app.data.database.Converters

@Entity(tableName = "notification_preferences")
@TypeConverters(Converters::class)
data class NotificationPreferences(
    @PrimaryKey
    val userId: String = "default_user",

    // 通知总开关
    val notificationsEnabled: Boolean = true,

    // 勿扰模式设置
    val dndEnabled: Boolean = false,
    val dndStartHour: Int = 22,
    val dndStartMinute: Int = 0,
    val dndEndHour: Int = 7,
    val dndEndMinute: Int = 0,

    // 通知类型开关
    val flightStatusEnabled: Boolean = true,
    val flightDelayEnabled: Boolean = true,
    val flightCancellationEnabled: Boolean = true,
    val boardingTimeEnabled: Boolean = true,
    val gateChangeEnabled: Boolean = true,
    val baggageStatusEnabled: Boolean = true,
    val priceAlertEnabled: Boolean = true,
    val tripReminderEnabled: Boolean = true,
    val weatherAlertEnabled: Boolean = true,

    // 关注的航班号列表
    val watchedFlightNumbers: List<String> = emptyList(),

    // 关注的机场列表
    val watchedAirports: List<String> = emptyList(),

    // 关注的航空公司列表
    val watchedAirlines: List<String> = emptyList(),

    // 通知频率设置
    val notificationFrequency: NotificationFrequency = NotificationFrequency.IMMEDIATE,

    // 高级设置
    val vibrationEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val ledEnabled: Boolean = true,

    // 定时通知设置
    val scheduledNotifications: List<ScheduledNotification> = emptyList(),

    // 最后更新时间
    val lastUpdated: Long = System.currentTimeMillis(),
)

enum class NotificationFrequency {
    IMMEDIATE, // 即时通知
    HOURLY, // 每小时汇总
    DAILY, // 每日汇总
    WEEKLY, // 每周汇总
}

data class ScheduledNotification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val scheduledTime: Long,
    val isActive: Boolean = true,
    val repeatInterval: Long? = null, // 重复间隔（毫秒），null表示一次性
)
