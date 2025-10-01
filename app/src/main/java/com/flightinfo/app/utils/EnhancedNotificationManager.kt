package com.flightinfo.app.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.flightinfo.app.R
import com.flightinfo.app.data.dao.NotificationHistoryDao
import com.flightinfo.app.data.dao.NotificationPreferencesDao
import com.flightinfo.app.data.model.NotificationCategory
import com.flightinfo.app.data.model.NotificationHistory
import com.flightinfo.app.data.model.NotificationPreferences
import com.flightinfo.app.data.model.NotificationPriority
import com.flightinfo.app.data.model.NotificationType
import com.flightinfo.app.data.model.ScheduledNotification
import com.flightinfo.app.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 增强通知管理器
 * 提供个性化的通知管理、分类、定时和历史记录功能
 */
@Singleton
class EnhancedNotificationManager @Inject constructor(
    private val context: Context,
    private val notificationHistoryDao: NotificationHistoryDao,
    private val notificationPreferencesDao: NotificationPreferencesDao,
) {

    companion object {
        const val FLIGHT_STATUS_CHANNEL_ID = "flight_status_channel"
        const val BAGGAGE_CHANNEL_ID = "baggage_channel"
        const val PRICE_ALERT_CHANNEL_ID = "price_alert_channel"
        const val TRIP_REMINDER_CHANNEL_ID = "trip_reminder_channel"
        const val WEATHER_CHANNEL_ID = "weather_channel"
        const val SYSTEM_CHANNEL_ID = "system_channel"

        private const val NOTIFICATION_ID_BASE = 20000
    }

    private val notificationManager = NotificationManagerCompat.from(context)
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    FLIGHT_STATUS_CHANNEL_ID,
                    "航班状态通知",
                    NotificationManager.IMPORTANCE_DEFAULT,
                ).apply {
                    description = "接收航班状态变更通知"
                },
                NotificationChannel(
                    BAGGAGE_CHANNEL_ID,
                    "行李跟踪通知",
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    description = "接收行李状态更新通知"
                },
                NotificationChannel(
                    PRICE_ALERT_CHANNEL_ID,
                    "价格提醒通知",
                    NotificationManager.IMPORTANCE_DEFAULT,
                ).apply {
                    description = "接收航班价格变动通知"
                },
                NotificationChannel(
                    TRIP_REMINDER_CHANNEL_ID,
                    "行程提醒通知",
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    description = "接收行程和登机提醒通知"
                },
                NotificationChannel(
                    WEATHER_CHANNEL_ID,
                    "天气提醒通知",
                    NotificationManager.IMPORTANCE_LOW,
                ).apply {
                    description = "接收天气相关通知"
                },
                NotificationChannel(
                    SYSTEM_CHANNEL_ID,
                    "系统通知",
                    NotificationManager.IMPORTANCE_DEFAULT,
                ).apply {
                    description = "接收系统更新和其他通知"
                },
            )

            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            channels.forEach { nm.createNotificationChannel(it) }
        }
    }

    /**
     * 发送航班状态通知
     */
    suspend fun sendFlightStatusNotification(
        flightNumber: String,
        status: String,
        type: NotificationType = NotificationType.FLIGHT_STATUS,
        priority: NotificationPriority = NotificationPriority.NORMAL,
        extraData: Map<String, String>? = null,
    ) {
        val preferences = notificationPreferencesDao.getPreferencesSync() ?: return

        if (!shouldSendNotification(type, preferences)) return

        val title = "航班状态更新"
        val message = "航班 $flightNumber 状态变更为：$status"

        val notification = NotificationCompat.Builder(context, FLIGHT_STATUS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_flight)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(getNotificationPriority(priority))
            .setContentIntent(createMainActivityPendingIntent())
            .setAutoCancel(true)
            .setVibrate(if (preferences.vibrationEnabled) longArrayOf(0, 250, 250, 250) else null)
            .apply {
                if (!preferences.soundEnabled) {
                    setSilent(true)
                }
            }
            .build()

        val notificationId = flightNumber.hashCode() + NOTIFICATION_ID_BASE
        notificationManager.notify(notificationId, notification)

        // 保存到历史记录
        saveNotificationHistory(
            title = title,
            message = message,
            type = type,
            category = NotificationCategory.FLIGHT_INFO,
            flightNumber = flightNumber,
            priority = priority,
            extraData = extraData,
        )
    }

    /**
     * 发送行李状态通知
     */
    suspend fun sendBaggageStatusNotification(
        baggageTagNumber: String,
        status: String,
        flightNumber: String? = null,
        priority: NotificationPriority = NotificationPriority.HIGH,
        extraData: Map<String, String>? = null,
    ) {
        val preferences = notificationPreferencesDao.getPreferencesSync() ?: return

        if (!preferences.baggageStatusEnabled) return

        val title = "行李状态更新"
        val message = "行李标签 $baggageTagNumber 状态：$status"

        val notification = NotificationCompat.Builder(context, BAGGAGE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_flight)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(getNotificationPriority(priority))
            .setContentIntent(createMainActivityPendingIntent())
            .setAutoCancel(true)
            .setVibrate(if (preferences.vibrationEnabled) longArrayOf(0, 250, 250, 250) else null)
            .build()

        val notificationId = baggageTagNumber.hashCode() + NOTIFICATION_ID_BASE + 1000
        notificationManager.notify(notificationId, notification)

        // 保存到历史记录
        saveNotificationHistory(
            title = title,
            message = message,
            type = NotificationType.BAGGAGE_STATUS,
            category = NotificationCategory.BAGGAGE_TRACKING,
            flightNumber = flightNumber,
            baggageTagNumber = baggageTagNumber,
            baggageStatus = status,
            priority = priority,
            extraData = extraData,
        )
    }

    /**
     * 发送价格提醒通知
     */
    suspend fun sendPriceAlertNotification(
        flightNumber: String,
        newPrice: Double,
        originalPrice: Double,
        priority: NotificationPriority = NotificationPriority.NORMAL,
        extraData: Map<String, String>? = null,
    ) {
        val preferences = notificationPreferencesDao.getPreferencesSync() ?: return

        if (!preferences.priceAlertEnabled) return

        val title = "价格提醒"
        val message = "航班 $flightNumber 价格变动：$originalPrice → $newPrice"

        val notification = NotificationCompat.Builder(context, PRICE_ALERT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_flight)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(getNotificationPriority(priority))
            .setContentIntent(createMainActivityPendingIntent())
            .setAutoCancel(true)
            .setVibrate(if (preferences.vibrationEnabled) longArrayOf(0, 250, 250, 250) else null)
            .build()

        val notificationId = flightNumber.hashCode() + NOTIFICATION_ID_BASE + 2000
        notificationManager.notify(notificationId, notification)

        // 保存到历史记录
        saveNotificationHistory(
            title = title,
            message = message,
            type = NotificationType.PRICE_ALERT,
            category = NotificationCategory.PRICE_TRACKING,
            flightNumber = flightNumber,
            priority = priority,
            extraData = extraData,
        )
    }

    /**
     * 发送定时通知
     */
    fun scheduleNotification(
        scheduledNotification: ScheduledNotification,
        delayInMillis: Long = 0,
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, com.flightinfo.app.receiver.NotificationAlarmReceiver::class.java).apply {
            putExtra("notification_id", scheduledNotification.id)
            putExtra("title", scheduledNotification.title)
            putExtra("message", scheduledNotification.message)
            putExtra("type", scheduledNotification.type.name)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            scheduledNotification.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val triggerTime = scheduledNotification.scheduledTime + delayInMillis

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent,
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent,
            )
        }
    }

    /**
     * 检查是否应该发送通知
     */
    private suspend fun shouldSendNotification(
        type: NotificationType,
        preferences: NotificationPreferences,
    ): Boolean {
        if (!preferences.notificationsEnabled) return false

        // 基于数据库中的 DND 设置判断免打扰（支持跨天时间段）
        if (preferences.dndEnabled) {
            val now = java.util.Calendar.getInstance()
            val start = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, preferences.dndStartHour)
                set(java.util.Calendar.MINUTE, preferences.dndStartMinute)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }
            val end = java.util.Calendar.getInstance().apply {
                set(java.util.Calendar.HOUR_OF_DAY, preferences.dndEndHour)
                set(java.util.Calendar.MINUTE, preferences.dndEndMinute)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }

            val isDnd = if (start.after(end)) {
                now.after(start) || now.before(end)
            } else {
                now.after(start) && now.before(end)
            }
            if (isDnd) return false
        }

        // 检查特定类型通知开关
        return when (type) {
            NotificationType.FLIGHT_STATUS -> preferences.flightStatusEnabled
            NotificationType.FLIGHT_DELAY -> preferences.flightDelayEnabled
            NotificationType.FLIGHT_CANCELLED -> preferences.flightCancellationEnabled
            NotificationType.BOARDING_TIME -> preferences.boardingTimeEnabled
            NotificationType.GATE_CHANGE -> preferences.gateChangeEnabled
            NotificationType.BAGGAGE_STATUS -> preferences.baggageStatusEnabled
            NotificationType.PRICE_ALERT -> preferences.priceAlertEnabled
            NotificationType.TRIP_REMINDER -> preferences.tripReminderEnabled
            NotificationType.WEATHER_ALERT -> preferences.weatherAlertEnabled
            NotificationType.CUSTOM -> true
        }
    }

    /**
     * 保存通知历史记录
     */
    private suspend fun saveNotificationHistory(
        title: String,
        message: String,
        type: NotificationType,
        category: NotificationCategory,
        flightNumber: String? = null,
        departureAirport: String? = null,
        arrivalAirport: String? = null,
        airline: String? = null,
        baggageTagNumber: String? = null,
        baggageStatus: String? = null,
        priority: NotificationPriority = NotificationPriority.NORMAL,
        extraData: Map<String, String>? = null,
    ) {
        val notification = NotificationHistory(
            title = title,
            message = message,
            type = type,
            category = category,
            flightNumber = flightNumber,
            departureAirport = departureAirport,
            arrivalAirport = arrivalAirport,
            airline = airline,
            baggageTagNumber = baggageTagNumber,
            baggageStatus = baggageStatus,
            priority = priority,
            extraData = extraData,
        )

        scope.launch {
            try {
                notificationHistoryDao.insertNotification(notification)
            } catch (e: Exception) {
                PerformanceMonitor.logError("NotificationHistory", e.message ?: "Unknown error")
            }
        }
    }

    private fun getNotificationPriority(priority: NotificationPriority): Int {
        return when (priority) {
            NotificationPriority.LOW -> NotificationCompat.PRIORITY_LOW
            NotificationPriority.NORMAL -> NotificationCompat.PRIORITY_DEFAULT
            NotificationPriority.HIGH -> NotificationCompat.PRIORITY_HIGH
            NotificationPriority.URGENT -> NotificationCompat.PRIORITY_MAX
        }
    }

    private fun createMainActivityPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }
}
