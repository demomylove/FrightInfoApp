package com.flightinfo.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process
import androidx.core.app.NotificationCompat
import com.flightinfo.app.R
import com.flightinfo.app.data.model.FlightInfo
import com.flightinfo.app.data.repository.FlightRepository
import com.flightinfo.app.ui.MainActivity
import com.flightinfo.app.utils.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FlightStatusCheckService : Service() {
    companion object {
        private const val CHANNEL_ID = "flight_status_check_channel"
        private const val CHANNEL_NAME = "Flight Status Checks"
        private const val CHANNEL_DESCRIPTION = "Background flight status checking"
        private const val NOTIFICATION_ID = 1001
        private const val CHECK_INTERVAL: Long = 60000 // 1 minute
    }

    @Inject
    lateinit var flightRepository: FlightRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var handlerThread: HandlerThread
    private lateinit var serviceHandler: ServiceHandler

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            // Check flight statuses periodically
            checkFlightStatuses()
            
            // Schedule next check
            serviceHandler.sendEmptyMessageDelayed(0, CHECK_INTERVAL)
        }
    }

    override fun onCreate() {
        super.onCreate()
        
        // Create a background thread for handling flight status checks
        handlerThread = HandlerThread("FlightStatusCheckService", Process.THREAD_PRIORITY_BACKGROUND)
        handlerThread.start()
        
        // Get the HandlerThread's Looper and use it for our Handler
        val looper = handlerThread.looper
        serviceHandler = ServiceHandler(looper)
        
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start the periodic flight status check
        serviceHandler.sendEmptyMessage(0)
        
        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        handlerThread.quitSafely()
    }

    private fun checkFlightStatuses() {
        // In a real implementation, you would:
        // 1. Get list of tracked flights from database
        // 2. Check each flight's current status
        // 3. Compare with previous status
        // 4. If status changed, send notification
        
        // For demonstration, we'll just log that we're checking
        // In a real app, you would implement the actual checking logic here
        serviceScope.launch {
            // This is where you would check actual flight statuses
            // For example:
            // val trackedFlights = trackedFlightRepository.getAllTrackedFlights()
            // for (flight in trackedFlights) {
            //     val currentStatus = flightRepository.getFlightDetails(flight.flightNumber)
            //     if (currentStatus.isSuccessful && currentStatus.body() != null) {
            //         val newStatus = currentStatus.body()!!.status
            //         if (newStatus != flight.lastStatus) {
            //             sendFlightStatusNotification(flight.flightNumber, newStatus)
            //             // Update the tracked flight status in database
            //             flightRepository.updateTrackedFlightStatus(flight.flightId, newStatus)
            //         }
            //     }
            // }
        }
    }

    private fun sendFlightStatusNotification(flightNumber: String, newStatus: String) {
        val title = "Flight $flightNumber Status Update"
        val message = "Your flight status has changed to $newStatus"
        
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
            notify(flightNumber.hashCode(), notificationBuilder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): android.app.Notification {
        // Create an intent that will be fired when the user taps the notification
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, 
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_flight)
            .setContentTitle("Checking Flight Statuses")
            .setContentText("Monitoring your tracked flights")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .build()
    }
}