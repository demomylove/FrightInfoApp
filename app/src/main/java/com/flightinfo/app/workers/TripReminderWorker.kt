package com.flightinfo.app.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.flightinfo.app.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TripReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationHelper: NotificationHelper,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val KEY_FLIGHT_NUMBER = "flight_number"
        const val KEY_DEPARTURE_AIRPORT = "departure_airport"
        const val KEY_REMINDER_TEXT = "reminder_text"
    }

    override suspend fun doWork(): Result {
        val flightNumber = inputData.getString(KEY_FLIGHT_NUMBER) ?: return Result.failure()
        val departureAirport = inputData.getString(KEY_DEPARTURE_AIRPORT) ?: ""
        val reminderText = inputData.getString(KEY_REMINDER_TEXT) ?: "It's time to leave for the airport."

        notificationHelper.showTripReminderNotification(
            flightNumber,
            departureAirport,
            reminderText,
        )

        return Result.success()
    }
}
