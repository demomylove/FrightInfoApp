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
    companion object {
        private const val CHANNEL_ID = "flight_status_channel"
        private const val CHANNEL_NAME = "Flight Status Updates"
        private const val CHANNEL_DESCRIPTION = "Notifications for flight status changes"
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
            showNotification(it.title ?: "Flight Status Update", it.body ?: "")
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val flightNumber = data["flightNumber"]
        val status = data["status"]
        val message = data["message"]
        
        if (flightNumber != null && status != null) {
            showNotification(
                "Flight $flightNumber Status Update",
                message ?: "Your flight status has changed to $status"
            )
        }
    }

    private fun showNotification(title: String, message: String) {
        createNotificationChannel()
        
        // Create an intent that will be fired when the user taps the notification
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, 
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_flight) // You'll need to add this drawable
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onNewToken(token: String) {
        // Handle new token
        // Send token to your server
    }
}