package com.flightinfo.app.utils

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import com.flightinfo.app.data.model.Itinerary

class CalendarSyncManager(private val context: Context) {

    fun canWriteCalendar(): Boolean {
        val write = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
        val read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
        return write && read
    }

    fun syncItinerary(itinerary: Itinerary): Boolean {
        if (!canWriteCalendar()) return false
        val cr = context.contentResolver
        val calendarId = getPrimaryCalendarId(cr) ?: return false

        // 主行程事件
        insertEvent(
            cr,
            calendarId,
            title = "${itinerary.title} (${itinerary.passengers.joinToString()})",
            description = buildString {
                append("PNR: ${itinerary.pnr ?: "-"}\n")
                append("航段: ${itinerary.segments.size} 段\n")
            },
            start = itinerary.startTime,
            end = itinerary.endTime,
        )

        // 各航段作为子事件
        itinerary.segments.forEach { seg ->
            insertEvent(
                cr,
                calendarId,
                title = "航班 ${seg.flightNumber} ${seg.departureAirport}→${seg.arrivalAirport}",
                description = "航司: ${seg.airline ?: "-"}  T:${seg.terminal ?: "-"} G:${seg.gate ?: "-"}",
                start = seg.departureTime,
                end = seg.arrivalTime,
            )
        }

        // 待办作为提醒事件
        itinerary.tasks.forEach { task ->
            insertEvent(
                cr,
                calendarId,
                title = "[待办] ${task.title}",
                description = "行程: ${itinerary.title}",
                start = task.dueTime,
                end = task.dueTime + 15 * 60 * 1000L,
                hasAlarm = true,
                minutesBefore = 0,
            )
        }
        return true
    }

    private fun insertEvent(
        cr: ContentResolver,
        calendarId: Long,
        title: String,
        description: String,
        start: Long,
        end: Long,
        hasAlarm: Boolean = false,
        minutesBefore: Int = 10,
    ): Uri? {
        val values = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.DTSTART, start)
            put(CalendarContract.Events.DTEND, end)
            put(CalendarContract.Events.EVENT_TIMEZONE, java.util.TimeZone.getDefault().id)
        }
        val uri = cr.insert(CalendarContract.Events.CONTENT_URI, values)
        if (hasAlarm && uri != null) {
            val eventId = uri.lastPathSegment?.toLongOrNull() ?: return uri
            val reminderValues = ContentValues().apply {
                put(CalendarContract.Reminders.EVENT_ID, eventId)
                put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
                put(CalendarContract.Reminders.MINUTES, minutesBefore)
            }
            cr.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues)
        }
        return uri
    }

    private fun getPrimaryCalendarId(cr: ContentResolver): Long? {
        val projection = arrayOf(
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.IS_PRIMARY,
            CalendarContract.Calendars.VISIBLE,
        )
        val uri = CalendarContract.Calendars.CONTENT_URI
        val cursor = cr.query(uri, projection, null, null, null) ?: return null
        cursor.use { c ->
            var firstId: Long? = null
            while (c.moveToNext()) {
                val id = c.getLong(0)
                val isPrimary = if (!c.isNull(1)) c.getInt(1) == 1 else false
                val visible = c.getInt(2) == 1
                if (firstId == null) firstId = id
                if (isPrimary && visible) return id
            }
            return firstId
        }
    }
}
