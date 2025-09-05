package com.flightinfo.app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.flightinfo.app.R
import com.flightinfo.app.ui.MainActivity

class NotificationHelper(private val context: Context) {
    companion object {
        const val FLIGHT_STATUS_CHANNEL_ID = "flight_status_channel"
        const val TRIP_REMINDER_CHANNEL_ID = "trip_reminder_channel"
        private const val NOTIFICATION_ID_BASE = 10000
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val statusChannel = NotificationChannel(
                FLIGHT_STATUS_CHANNEL_ID,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = context.getString(R.string.notification_channel_description)
            }

            val reminderChannel = NotificationChannel(
                TRIP_REMINDER_CHANNEL_ID,
                context.getString(R.string.trip_reminder_channel_name),
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = context.getString(R.string.trip_reminder_channel_description)
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(statusChannel)
            notificationManager.createNotificationChannel(reminderChannel)
        }
    }

    fun showFlightStatusNotification(flightNumber: String, newStatus: String) {
        val title = context.getString(R.string.flight_status_update)
        val message = context.getString(R.string.flight_status_changed, newStatus)

        val pendingIntent = createMainActivityPendingIntent()

        val notificationBuilder = NotificationCompat.Builder(context, FLIGHT_STATUS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_flight)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(flightNumber.hashCode() + NOTIFICATION_ID_BASE, notificationBuilder.build())
        }
    }

    fun showFlightPriceNotification(flightNumber: String, newPrice: Double) {
        val title = "Price Alert for flight $flightNumber"
        val message = "The price has changed to $newPrice"

        val pendingIntent = createMainActivityPendingIntent()

        val notificationBuilder = NotificationCompat.Builder(context, FLIGHT_STATUS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_flight)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(flightNumber.hashCode() + NOTIFICATION_ID_BASE + 1, notificationBuilder.build())
        }
    }

    fun showTripReminderNotification(flightNumber: String, departureAirport: String, reminderText: String) {
        val title = "Reminder for Flight $flightNumber"
        val message = "Leaving from $departureAirport. $reminderText"

        val pendingIntent = createMainActivityPendingIntent()

        val notificationBuilder = NotificationCompat.Builder(context, TRIP_REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_car) // Using a different icon for trip reminders
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // Use a unique ID for trip reminders
            notify(flightNumber.hashCode() + NOTIFICATION_ID_BASE + 2, notificationBuilder.build())
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
