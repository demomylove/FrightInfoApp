package com.flightinfo.app.utils

import com.flightinfo.app.data.model.NotificationHistory
import com.flightinfo.app.data.model.NotificationPreferences
import com.flightinfo.app.data.model.NotificationPriority
import com.flightinfo.app.data.model.NotificationType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

/**
 * 智能通知过滤器
 * 根据用户行为模式、偏好设置和时间因素智能筛选通知
 */
object IntelligentNotificationFilter {

    /**
     * 应用智能过滤规则
     */
    fun applyIntelligentFilter(
        notifications: Flow<List<NotificationHistory>>,
        preferences: Flow<NotificationPreferences>,
        userBehavior: Flow<UserBehaviorData>,
    ): Flow<List<FilteredNotification>> {
        return combine(
            notifications,
            preferences,
            userBehavior,
        ) { notifs, prefs, behavior ->
            notifs.map { notification ->
                filterNotification(notification, prefs, behavior)
            }.filterNotNull()
        }
    }

    /**
     * 过滤单个通知
     */
    private fun filterNotification(
        notification: NotificationHistory,
        preferences: NotificationPreferences,
        behavior: UserBehaviorData,
    ): FilteredNotification? {
        // 1. 基础过滤：检查通知开关和勿扰模式
        if (!shouldSendNotification(notification, preferences)) {
            return null
        }

        // 2. 智能过滤：基于用户行为模式
        val relevanceScore = calculateRelevanceScore(notification, behavior)

        // 3. 优先级调整：根据相关性和紧急程度调整优先级
        val adjustedPriority = adjustPriority(notification, relevanceScore)

        // 4. 时间过滤：避免频繁通知同一类型的内容
        if (isTooFrequent(notification, preferences)) {
            return null
        }

        return FilteredNotification(
            notification = notification,
            relevanceScore = relevanceScore,
            adjustedPriority = adjustedPriority,
            shouldShow = relevanceScore >= getThreshold(preferences),
        )
    }

    /**
     * 判断是否应该发送通知（基础过滤）
     */
    private fun shouldSendNotification(
        notification: NotificationHistory,
        preferences: NotificationPreferences,
    ): Boolean {
        if (!preferences.notificationsEnabled) return false

        // 检查勿扰模式（支持跨天时段）
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
        return when (notification.type) {
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
     * 计算通知相关性得分
     */
    private fun calculateRelevanceScore(
        notification: NotificationHistory,
        behavior: UserBehaviorData,
    ): Double {
        var score = 0.0

        // 1. 基于关注列表的相关性
        if (notification.flightNumber != null &&
            behavior.watchedFlights.contains(notification.flightNumber)
        ) {
            score += 0.4
        }

        if (notification.departureAirport != null &&
            behavior.watchedAirports.contains(notification.departureAirport)
        ) {
            score += 0.3
        }

        if (notification.arrivalAirport != null &&
            behavior.watchedAirports.contains(notification.arrivalAirport)
        ) {
            score += 0.3
        }

        if (notification.airline != null &&
            behavior.watchedAirlines.contains(notification.airline)
        ) {
            score += 0.2
        }

        // 2. 基于历史行为的相关性
        if (behavior.frequentlyAccessedTypes.contains(notification.type)) {
            score += 0.2
        }

        // 3. 基于时间因素的相关性
        val hoursSinceNotification = (System.currentTimeMillis() - notification.timestamp) / (1000 * 60 * 60)
        if (hoursSinceNotification < 24) {
            score += 0.1 // 24小时内的通知更有相关性
        }

        // 4. 基于优先级的相关性
        score += when (notification.priority) {
            NotificationPriority.LOW -> 0.0
            NotificationPriority.NORMAL -> 0.1
            NotificationPriority.HIGH -> 0.2
            NotificationPriority.URGENT -> 0.3
        }

        return score.coerceIn(0.0, 1.0)
    }

    /**
     * 调整通知优先级
     */
    private fun adjustPriority(
        notification: NotificationHistory,
        relevanceScore: Double,
    ): NotificationPriority {
        var priority = notification.priority

        // 如果相关性很高，提升优先级
        if (relevanceScore >= 0.7) {
            priority = when (priority) {
                NotificationPriority.LOW -> NotificationPriority.NORMAL
                NotificationPriority.NORMAL -> NotificationPriority.HIGH
                NotificationPriority.HIGH -> NotificationPriority.URGENT
                NotificationPriority.URGENT -> NotificationPriority.URGENT
            }
        }

        // 如果相关性很低，降低优先级
        if (relevanceScore <= 0.3) {
            priority = when (priority) {
                NotificationPriority.URGENT -> NotificationPriority.HIGH
                NotificationPriority.HIGH -> NotificationPriority.NORMAL
                NotificationPriority.NORMAL -> NotificationPriority.LOW
                NotificationPriority.LOW -> NotificationPriority.LOW
            }
        }

        return priority
    }

    /**
     * 检查通知是否过于频繁
     */
    private fun isTooFrequent(
        notification: NotificationHistory,
        preferences: NotificationPreferences,
    ): Boolean {
        // 这里可以实现更复杂的频率控制逻辑
        // 例如：同一航班的延误通知不要过于频繁
        return false
    }

    /**
     * 获取相关性阈值
     */
    private fun getThreshold(preferences: NotificationPreferences): Double {
        // 根据用户设置的敏感度调整阈值
        return when (preferences.notificationFrequency) {
            com.flightinfo.app.data.model.NotificationFrequency.IMMEDIATE -> 0.2
            com.flightinfo.app.data.model.NotificationFrequency.HOURLY -> 0.4
            com.flightinfo.app.data.model.NotificationFrequency.DAILY -> 0.6
            com.flightinfo.app.data.model.NotificationFrequency.WEEKLY -> 0.8
        }
    }

    /**
     * 用户行为数据类
     */
    data class UserBehaviorData(
        val watchedFlights: Set<String> = emptySet(),
        val watchedAirports: Set<String> = emptySet(),
        val watchedAirlines: Set<String> = emptySet(),
        val frequentlyAccessedTypes: Set<NotificationType> = emptySet(),
        val activeHours: Set<Int> = emptySet(),
        val preferredNotificationTypes: Set<NotificationType> = emptySet(),
    )

    /**
     * 过滤后的通知数据类
     */
    data class FilteredNotification(
        val notification: NotificationHistory,
        val relevanceScore: Double,
        val adjustedPriority: NotificationPriority,
        val shouldShow: Boolean,
    )
}
