package com.flightinfo.app.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NotificationConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromNotificationType(type: NotificationType): String = type.name

    @TypeConverter
    fun toNotificationType(type: String): NotificationType = NotificationType.valueOf(type)

    @TypeConverter
    fun fromNotificationCategory(category: NotificationCategory): String = category.name

    @TypeConverter
    fun toNotificationCategory(category: String): NotificationCategory = NotificationCategory.valueOf(category)

    @TypeConverter
    fun fromNotificationPriority(priority: NotificationPriority): String = priority.name

    @TypeConverter
    fun toNotificationPriority(priority: String): NotificationPriority = NotificationPriority.valueOf(priority)

    @TypeConverter
    fun fromScheduledNotifications(list: List<ScheduledNotification>): String = gson.toJson(list)

    @TypeConverter
    fun toScheduledNotifications(json: String): List<ScheduledNotification> {
        val type = object : TypeToken<List<ScheduledNotification>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    @TypeConverter
    fun fromStringMap(map: Map<String, String>?): String? = map?.let { gson.toJson(it) }

    @TypeConverter
    fun toStringMap(json: String?): Map<String, String>? {
        if (json.isNullOrEmpty()) return null
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson<Map<String, String>>(json, type)
    }
}
