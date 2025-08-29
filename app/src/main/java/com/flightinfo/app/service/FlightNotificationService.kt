package com.flightinfo.app.service

import com.flightinfo.app.utils.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlightNotificationService : FirebaseMessagingService() {
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Data messages are handled here, both in foreground and background.
        remoteMessage.data.let { data ->
            val title = data["title"]
            val message = data["message"]

            if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
                notificationHelper.showFlightStatusNotification(title, message)
            }
        }
    }

    override fun onNewToken(token: String) {
        // Handle new token
        // Send token to your server
    }
}
