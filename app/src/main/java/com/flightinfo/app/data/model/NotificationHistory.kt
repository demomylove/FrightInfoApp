package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.flightinfo.app.data.database.Converters

@Entity(tableName = "notification_history")
@TypeConverters(Converters::class)
data class NotificationHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // 通知基本信息
    val title: String,
    val message: String,
    val type: NotificationType,
    val category: NotificationCategory,

    // 关联的航班/机场信息
    val flightNumber: String? = null,
    val departureAirport: String? = null,
    val arrivalAirport: String? = null,
    val airline: String? = null,

    // 行李相关信息
    val baggageTagNumber: String? = null,
    val baggageStatus: String? = null,

    // 时间信息
    val timestamp: Long = System.currentTimeMillis(),
    val scheduledTime: Long? = null,

    // 通知状态
    val isRead: Boolean = false,
    val priority: NotificationPriority = NotificationPriority.NORMAL,

    // 附加数据
    val extraData: Map<String, String>? = null,
)

enum class NotificationType {
    FLIGHT_STATUS, // 航班状态变更
    FLIGHT_DELAY, // 航班延误
    FLIGHT_CANCELLED, // 航班取消
    BOARDING_TIME, // 登机时间提醒
    GATE_CHANGE, // 登机口变更
    BAGGAGE_STATUS, // 行李状态更新
    PRICE_ALERT, // 价格提醒
    TRIP_REMINDER, // 行程提醒
    WEATHER_ALERT, // 天气提醒
    CUSTOM, // 自定义通知
}

enum class NotificationCategory {
    FLIGHT_INFO, // 航班信息
    BAGGAGE_TRACKING, // 行李跟踪
    PRICE_TRACKING, // 价格跟踪
    TRIP_REMINDER, // 行程提醒
    WEATHER, // 天气提醒
    SYSTEM, // 系统通知
}

enum class NotificationPriority {
    LOW, // 低优先级
    NORMAL, // 普通优先级
    HIGH, // 高优先级
    URGENT, // 紧急优先级
}
