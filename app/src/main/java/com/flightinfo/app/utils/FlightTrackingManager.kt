package com.flightinfo.app.utils

import android.content.Context
import android.content.Intent
import com.flightinfo.app.service.FlightStatusCheckService

class FlightTrackingManager private constructor() {
    companion object {
        @Volatile
        private var INSTANCE: FlightTrackingManager? = null

        fun getInstance(): FlightTrackingManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FlightTrackingManager().also { INSTANCE = it }
            }
        }
    }

    fun startTrackingService(context: Context) {
        val intent = Intent(context, FlightStatusCheckService::class.java)
        context.startService(intent)
    }

    fun stopTrackingService(context: Context) {
        val intent = Intent(context, FlightStatusCheckService::class.java)
        context.stopService(intent)
    }
}