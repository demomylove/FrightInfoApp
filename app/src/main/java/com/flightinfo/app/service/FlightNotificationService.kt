package com.flightinfo.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.flightinfo.app.R
import com.flightinfo.app.ui.MainActivity
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
        
        // Check if message contains a data payload
        remoteMessage.data.isNotEmpty().let {
            // Handle data payload
            handleDataMessage(remoteMessage.data)
        }
        
        // Check if message contains a notification payload
        remoteMessage.notification?.let {
            // Handle notification payload
            notificationHelper.showFlightStatusNotification(
                "Flight", // This would typically be extracted from the notification
                it.body ?: "Flight status update"
            )
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val flightNumber = data["flightNumber"]
        val status = data["status"]
        val message = data["message"]
        
        if (flightNumber != null && status != null) {
            notificationHelper.showFlightStatusNotification(flightNumber, status)
        }
    }

    override fun onNewToken(token: String) {
        // Handle new token
        // Send token to your server
    }
}