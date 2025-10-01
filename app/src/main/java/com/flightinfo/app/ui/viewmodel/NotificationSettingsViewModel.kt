package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.model.NotificationPreferences
import com.flightinfo.app.data.model.NotificationType
import com.flightinfo.app.utils.NotificationSettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationSettingsViewModel @Inject constructor(
    private val settingsManager: NotificationSettingsManager,
) : ViewModel() {

    private val _notificationPreferences = MutableStateFlow<NotificationPreferences?>(null)
    val notificationPreferences: StateFlow<NotificationPreferences?> = _notificationPreferences

    private val _watchedFlights = MutableStateFlow<List<String>>(emptyList())
    val watchedFlights: StateFlow<List<String>> = _watchedFlights

    private val _watchedAirports = MutableStateFlow<List<String>>(emptyList())
    val watchedAirports: StateFlow<List<String>> = _watchedAirports

    private val _watchedAirlines = MutableStateFlow<List<String>>(emptyList())
    val watchedAirlines: StateFlow<List<String>> = _watchedAirlines

    fun loadNotificationSettings() {
        viewModelScope.launch {
            val prefs = settingsManager.getNotificationPreferences()
            _notificationPreferences.value = prefs
            _watchedFlights.value = prefs.watchedFlightNumbers
            _watchedAirports.value = prefs.watchedAirports
            _watchedAirlines.value = prefs.watchedAirlines
        }
    }

    fun updateNotificationSetting(type: NotificationType, enabled: Boolean) {
        viewModelScope.launch {
            when (type) {
                NotificationType.FLIGHT_STATUS -> settingsManager.setFlightStatusEnabled(enabled)
                NotificationType.FLIGHT_DELAY -> settingsManager.setFlightDelayEnabled(enabled)
                NotificationType.FLIGHT_CANCELLED -> settingsManager.setFlightCancellationEnabled(enabled)
                NotificationType.BOARDING_TIME -> settingsManager.setBoardingTimeEnabled(enabled)
                NotificationType.GATE_CHANGE -> settingsManager.setGateChangeEnabled(enabled)
                NotificationType.BAGGAGE_STATUS -> settingsManager.setBaggageStatusEnabled(enabled)
                NotificationType.PRICE_ALERT -> settingsManager.setPriceAlertEnabled(enabled)
                NotificationType.TRIP_REMINDER -> settingsManager.setTripReminderEnabled(enabled)
                NotificationType.WEATHER_ALERT -> settingsManager.setWeatherAlertEnabled(enabled)
                NotificationType.CUSTOM -> { /* no-op */ }
            }
            reload()
        }
    }

    fun addWatchedItem(type: String, item: String) {
        viewModelScope.launch {
            when (type) {
                "FLIGHT" -> settingsManager.addWatchedFlight(item)
                "AIRPORT" -> settingsManager.addWatchedAirport(item)
                "AIRLINE" -> settingsManager.addWatchedAirline(item)
            }
            reload()
        }
    }

    fun removeWatchedItem(type: String, item: String) {
        viewModelScope.launch {
            when (type) {
                "FLIGHT" -> settingsManager.removeWatchedFlight(item)
                "AIRPORT" -> settingsManager.removeWatchedAirport(item)
                "AIRLINE" -> settingsManager.removeWatchedAirline(item)
            }
            reload()
        }
    }

    fun updateDndSettings(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) {
        viewModelScope.launch {
            val enabled = _notificationPreferences.value?.dndEnabled ?: false
            settingsManager.updateDndSettings(enabled, startHour, startMinute, endHour, endMinute)
            reload()
        }
    }

    fun setDndEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val prefs = _notificationPreferences.value ?: settingsManager.getNotificationPreferences()
            settingsManager.updateDndSettings(enabled, prefs.dndStartHour, prefs.dndStartMinute, prefs.dndEndHour, prefs.dndEndMinute)
            reload()
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsManager.setNotificationsEnabled(enabled)
            reload()
        }
    }

    fun saveSettings() {
        // No-op for now; settings are persisted immediately on change
    }

    fun resetToDefault() {
        viewModelScope.launch {
            settingsManager.initializeDefaultPreferences()
            reload()
        }
    }

    private suspend fun reload() {
        val prefs = settingsManager.getNotificationPreferences()
        _notificationPreferences.value = prefs
        _watchedFlights.value = prefs.watchedFlightNumbers
        _watchedAirports.value = prefs.watchedAirports
        _watchedAirlines.value = prefs.watchedAirlines
    }
}
