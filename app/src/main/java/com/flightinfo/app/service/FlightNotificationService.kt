package com.flightinfo.app.service

import android.content.Context
import android.content.SharedPreferences
import com.flightinfo.app.utils.NotificationHelper
import com.flightinfo.app.utils.NotificationPolicy
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlightNotificationService : FirebaseMessagingService() {

    companion object Prefs {
        const val PREFS_NAME = "notification_prefs"
        const val KEY_GATE_CHANGE = "gate_change"
        const val KEY_DELAY = "delay"
        const val KEY_CANCELLATION = "cancellation"
        const val KEY_BOARDING_TIME = "boarding_time"
    }

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

    private fun isDndActive(): Boolean = NotificationPolicy.isDndActive(this)

    override fun onNewToken(token: String) {
        // Handle new token
        // Send token to your server
    }
}
