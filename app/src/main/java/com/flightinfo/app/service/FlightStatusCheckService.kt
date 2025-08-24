package com.flightinfo.app.service

import android.app.Notification
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
import com.flightinfo.app.data.repository.FlightRepository
import com.flightinfo.app.data.repository.TrackedFlightRepository
import com.flightinfo.app.ui.MainActivity
import com.flightinfo.app.utils.NotificationHelper
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FlightStatusCheckService : Service() {
    companion object {
        private const val CHANNEL_ID = "flight_status_check_channel"
        private const val CHANNEL_NAME = "Flight Status Checks"
        private const val CHANNEL_DESCRIPTION = "Background flight status checking"
        private const val NOTIFICATION_ID = 1001
        private const val CHECK_INTERVAL: Long = 60000 // 1 minute
        private const val FOREGROUND_SERVICE_TYPE_DATA_SYNC = 0x00000001
        private const val PENDING_INTENT_FLAGS = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        private const val ACTIVITY_FLAGS = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    @Inject
    lateinit var flightRepository: FlightRepository

    @Inject
    lateinit var trackedFlightRepository: TrackedFlightRepository

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var handlerThread: HandlerThread
    private lateinit var serviceHandler: ServiceHandler
    private lateinit var notificationHelper: NotificationHelper
    private var isServiceRunning = false

    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            if (isServiceRunning) {
                checkFlightStatuses()
                // Schedule next check
                serviceHandler.sendEmptyMessageDelayed(0, CHECK_INTERVAL)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        try {
            initializeService()
            startForegroundService()
            isServiceRunning = true
        } catch (e: Exception) {
            Timber.e(e, "Error creating FlightStatusCheckService")
            stopSelf()
        }
    }

    private fun initializeService() {
        notificationHelper = NotificationHelper(this)

        handlerThread = HandlerThread(
            "FlightStatusCheckService",
            Process.THREAD_PRIORITY_BACKGROUND,
        ).apply {
            start()
        }

        serviceHandler = ServiceHandler(handlerThread.looper)
        createNotificationChannel()
    }

    private fun startForegroundService() {
        val notification = createNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isServiceRunning) {
            serviceHandler.sendEmptyMessage(0)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanupResources()
    }

    private fun cleanupResources() {
        isServiceRunning = false
        serviceScope.cancel()
        try {
            handlerThread.quitSafely()
        } catch (e: Exception) {
            Timber.e(e, "Error quitting handler thread")
        }
    }

    private fun checkFlightStatuses() {
        serviceScope.launch {
            try {
                trackedFlightRepository.getAllTrackedFlights().collect { trackedFlights ->
                    if (trackedFlights.isNotEmpty()) {
                        processTrackedFlights(trackedFlights)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error checking flight statuses")
            }
        }
    }

    private suspend fun processTrackedFlights(trackedFlights: List<com.flightinfo.app.data.model.TrackedFlight>) {
        trackedFlights.forEach { flight ->
            serviceScope.launch {
                checkFlightStatusUpdates(flight)
                checkFlightPriceUpdates(flight)
            }
        }
    }

    private suspend fun checkFlightStatusUpdates(flight: com.flightinfo.app.data.model.TrackedFlight) {
        try {
            flightRepository.getFlightDetails(flight.flightNumber).collect { result ->
                if (result is Resource.Success) {
                    result.data?.let { flightDetails ->
                        if (flightDetails.status != flight.lastStatus) {
                            withContext(Dispatchers.Main) {
                                sendFlightStatusNotification(flight.flightNumber, flightDetails.status)
                            }
                            trackedFlightRepository.updateTrackedFlightStatus(flight.flightId, flightDetails.status)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error checking status for flight ${flight.flightNumber}")
        }
    }

    private suspend fun checkFlightPriceUpdates(flight: com.flightinfo.app.data.model.TrackedFlight) {
        try {
            flightRepository.getFlightPrice(flight.flightId).collect { result ->
                if (result is Resource.Success) {
                    result.data?.let { priceInfo ->
                        if (priceInfo.price != flight.lastPrice) {
                            withContext(Dispatchers.Main) {
                                sendFlightPriceNotification(flight.flightNumber, priceInfo.price)
                            }
                            trackedFlightRepository.updateTrackedFlightPrice(flight.flightId, priceInfo.price)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Error checking price for flight ${flight.flightNumber}")
        }
    }

    private fun sendFlightPriceNotification(flightNumber: String, newPrice: Double) {
        try {
            notificationHelper.showFlightPriceNotification(flightNumber, newPrice)
        } catch (e: Exception) {
            Timber.e(e, "Error sending price notification for flight $flightNumber")
        }
    }

    private fun sendFlightStatusNotification(flightNumber: String, newStatus: String) {
        try {
            notificationHelper.showFlightStatusNotification(flightNumber, newStatus)
        } catch (e: Exception) {
            Timber.e(e, "Error sending status notification for flight $flightNumber")
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW,
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return try {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = ACTIVITY_FLAGS
            }

            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PENDING_INTENT_FLAGS,
            )

            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_flight)
                .setContentTitle("Checking Flight Statuses")
                .setContentText("Monitoring your tracked flights")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .build()
        } catch (e: Exception) {
            Timber.e(e, "Error creating notification")
            // Return a basic notification as fallback
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_flight)
                .setContentTitle("Flight Status Monitor")
                .setContentText("Service running")
                .build()
        }
    }
}
