package com.flightinfo.app.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.flightinfo.app.data.model.TrackedFlight
import com.flightinfo.app.data.repository.FlightRepository
import com.flightinfo.app.data.repository.TrackedFlightRepository
import com.flightinfo.app.utils.NotificationHelper
import com.flightinfo.app.utils.NotificationPolicy
import com.flightinfo.app.utils.Resource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import timber.log.Timber

@HiltWorker
class FlightSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val flightRepository: FlightRepository,
    private val trackedFlightRepository: TrackedFlightRepository,
    private val notificationHelper: NotificationHelper,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            if (NotificationPolicy.isDndActive(applicationContext)) {
                Timber.d("DND active, skipping notifications this cycle")
            }

            val tracked = trackedFlightRepository.getAllTrackedFlights().first()
            if (tracked.isEmpty()) return Result.success()

            tracked.forEach { flight ->
                syncStatus(flight)
                syncPrice(flight)
            }
            return Result.success()
        } catch (e: Exception) {
            Timber.e(e, "FlightSyncWorker failed")
            return Result.retry()
        }
    }

    private suspend fun syncStatus(flight: TrackedFlight) {
        try {
            when (val res = flightRepository.getFlightDetails(flight.flightNumber).first()) {
                is Resource.Success -> {
                    val details = res.data ?: return
                    if (details.status != flight.lastStatus) {
                        if (!NotificationPolicy.isDndActive(applicationContext)) {
                            notificationHelper.showFlightStatusNotification(flight.flightNumber, details.status)
                        }
                        trackedFlightRepository.updateTrackedFlightStatus(flight.flightId, details.status)
                    }
                }
                else -> Unit
            }
        } catch (e: Exception) {
            Timber.e(e, "Status sync error for ${flight.flightNumber}")
        }
    }

    private suspend fun syncPrice(flight: TrackedFlight) {
        try {
            when (val res = flightRepository.getFlightPrice(flight.flightId).first()) {
                is Resource.Success -> {
                    val priceInfo = res.data ?: return
                    val last = flight.lastPrice
                    if (last == null || priceInfo.price != last) {
                        if (!NotificationPolicy.isDndActive(applicationContext)) {
                            notificationHelper.showFlightPriceNotification(flight.flightNumber, priceInfo.price)
                        }
                        trackedFlightRepository.updateTrackedFlightPrice(flight.flightId, priceInfo.price)
                    }
                }
                else -> Unit
            }
        } catch (e: Exception) {
            Timber.e(e, "Price sync error for ${flight.flightNumber}")
        }
    }
}
