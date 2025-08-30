package com.flightinfo.app.service

import android.content.Context
import android.content.SharedPreferences
import com.flightinfo.app.utils.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import com.flightinfo.app.ui.fragment.NotificationSettingsFragment.Companion as Prefs

@AndroidEntryPoint
class FlightNotificationService : FirebaseMessagingService() {

    private lateinit var notificationHelper: NotificationHelper
    private lateinit var prefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        prefs = getSharedPreferences(Prefs.PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.data.let { data ->
            val title = data["title"]
            val message = data["message"]
            val type = data["type"] // e.g., "gate_change", "delay", "cancellation"

            if (shouldShowNotification(type)) {
                if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
                    notificationHelper.showFlightStatusNotification(title, message)
                }
            }
        }
    }

    private fun shouldShowNotification(type: String?): Boolean {
        if (isDndActive()) {
            return false
        }

        return when (type) {
            "gate_change" -> prefs.getBoolean(Prefs.KEY_GATE_CHANGE, true)
            "delay" -> prefs.getBoolean(Prefs.KEY_DELAY, true)
            "cancellation" -> prefs.getBoolean(Prefs.KEY_CANCELLATION, true)
            "boarding_time" -> prefs.getBoolean(Prefs.KEY_BOARDING_TIME, true)
            // For generic or untyped notifications, show by default unless DND is on.
            else -> true
        }
    }

    private fun isDndActive(): Boolean {
        if (!prefs.getBoolean(Prefs.KEY_DO_NOT_DISTURB, false)) {
            return false
        }

        val startHour = prefs.getInt(Prefs.KEY_DND_START_HOUR, 22)
        val startMinute = prefs.getInt(Prefs.KEY_DND_START_MINUTE, 0)
        val endHour = prefs.getInt(Prefs.KEY_DND_END_HOUR, 7)
        val endMinute = prefs.getInt(Prefs.KEY_DND_END_MINUTE, 0)

        val now = Calendar.getInstance()
        val start = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, startHour)
            set(Calendar.MINUTE, startMinute)
        }
        val end = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, endHour)
            set(Calendar.MINUTE, endMinute)
        }

        // Handle overnight DND period (e.g., 10 PM to 7 AM)
        if (start.after(end)) {
            return now.after(start) || now.before(end)
        }
        return now.after(start) && now.before(end)
    }

    override fun onNewToken(token: String) {
        // Handle new token
        // Send token to your server
    }
}
