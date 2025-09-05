package com.flightinfo.app.utils

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar

object NotificationPolicy {
    private const val PREFS_NAME = "NotificationPrefs"
    private const val KEY_DO_NOT_DISTURB = "dnd_enabled"
    private const val KEY_DND_START_HOUR = "dnd_start_hour"
    private const val KEY_DND_START_MINUTE = "dnd_start_minute"
    private const val KEY_DND_END_HOUR = "dnd_end_hour"
    private const val KEY_DND_END_MINUTE = "dnd_end_minute"

    fun isDndActive(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (!prefs.getBoolean(KEY_DO_NOT_DISTURB, false)) return false

        val startHour = prefs.getInt(KEY_DND_START_HOUR, 22)
        val startMinute = prefs.getInt(KEY_DND_START_MINUTE, 0)
        val endHour = prefs.getInt(KEY_DND_END_HOUR, 7)
        val endMinute = prefs.getInt(KEY_DND_END_MINUTE, 0)

        val now = Calendar.getInstance()
        val start = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, startHour)
            set(Calendar.MINUTE, startMinute)
        }
        val end = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, endHour)
            set(Calendar.MINUTE, endMinute)
        }

        return if (start.after(end)) {
            now.after(start) || now.before(end)
        } else {
            now.after(start) && now.before(end)
        }
    }
}
