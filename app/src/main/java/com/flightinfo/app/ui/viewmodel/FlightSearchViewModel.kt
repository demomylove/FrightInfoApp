package com.flightinfo.app.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.flightinfo.app.data.model.FlightInfo
import com.flightinfo.app.data.model.FlightSearchResponse
import com.flightinfo.app.data.model.PriceRangeFilter
import com.flightinfo.app.data.model.TrackedFlight
import com.flightinfo.app.data.model.TravelSuggestionResponse
import com.flightinfo.app.data.repository.BookmarkedFlightRepository
import com.flightinfo.app.data.repository.FlightRepository
import com.flightinfo.app.data.repository.TrackedFlightRepository
import com.flightinfo.app.ui.fragment.NotificationSettingsFragment
import com.flightinfo.app.utils.Resource
import com.flightinfo.app.workers.TripReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val application: Application,
    private val repository: FlightRepository,
    private val trackedFlightRepository: TrackedFlightRepository,
    private val bookmarkedFlightRepository: BookmarkedFlightRepository,
) : ViewModel() {

    private val _searchResults = MutableStateFlow<Resource<FlightSearchResponse>>(Resource.Idle())
    val searchResults: StateFlow<Resource<FlightSearchResponse>> = _searchResults.asStateFlow()

    private val _realtimeFlights = MutableStateFlow<Resource<FlightSearchResponse>>(Resource.Loading())
    val realtimeFlights: StateFlow<Resource<FlightSearchResponse>> = _realtimeFlights.asStateFlow()

    private val _travelSuggestions = MutableStateFlow<Resource<TravelSuggestionResponse>>(Resource.Loading())
    val travelSuggestions: StateFlow<Resource<TravelSuggestionResponse>> = _travelSuggestions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _priceRangeFilter = MutableStateFlow<PriceRangeFilter?>(null)
    val priceRangeFilter: StateFlow<PriceRangeFilter?> = _priceRangeFilter.asStateFlow()

    val bookmarkedFlights: StateFlow<List<String>> =
        bookmarkedFlightRepository.getAllBookmarkedFlights()
            .map { flights -> flights.map { it.flightNumber } }
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Filtered search results that apply price range filtering
    val filteredSearchResults = _searchResults.combine(_priceRangeFilter) { results, filter ->
        when (results) {
            is Resource.Success -> {
                val filteredFlights = results.data?.flights?.filter { flight ->
                    filter?.isPriceInRange(flight.price ?: 0.0) ?: true
                }
                Resource.Success(results.data?.copy(flights = filteredFlights ?: emptyList()))
            }
            else -> results
        }
    }

    init {
        loadRealtimeFlights()
    }

    fun searchFlights(
        flightNumber: String? = null,
        departureAirport: String? = null,
        arrivalAirport: String? = null,
        date: String? = null,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.searchFlights(flightNumber, departureAirport, arrivalAirport, date)
                .collect { result ->
                    _searchResults.value = result
                    _isLoading.value = false
                }
        }
    }

    fun loadRealtimeFlights() {
        viewModelScope.launch {
            repository.getRealtimeFlights()
                .collect { result ->
                    _realtimeFlights.value = result
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearResults() {
        _searchResults.value = Resource.Loading()
    }

    fun refreshRealtimeFlights() {
        loadRealtimeFlights()
    }
    fun loadTravelSuggestions(destination: String) {
        viewModelScope.launch {
            repository.getTravelSuggestions(destination)
                .collect { result ->
                    _travelSuggestions.value = result
                }
        }
    }

    fun trackFlight(flight: FlightInfo) {
        viewModelScope.launch {
            val trackedFlight = TrackedFlight(
                flightId = flight.flightNumber, // Use flightNumber as the unique ID
                flightNumber = flight.flightNumber,
                lastStatus = flight.status,
                lastUpdated = System.currentTimeMillis(),
                notificationEnabled = true,
            )
            trackedFlightRepository.insertTrackedFlight(trackedFlight)
            scheduleTripReminder(flight)
        }
    }

    private fun scheduleTripReminder(flight: FlightInfo) {
        val prefs = application.getSharedPreferences(NotificationSettingsFragment.PREFS_NAME, Context.MODE_PRIVATE)
        val isReminderEnabled = prefs.getBoolean(NotificationSettingsFragment.KEY_LEAVE_FOR_AIRPORT, false)

        if (isReminderEnabled) {
            try {
                // Assuming ISO 8601 format "yyyy-MM-dd'T'HH:mm:ss'Z'"
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                val departureTimeMillis = sdf.parse(flight.departureTime)?.time ?: return

                val reminderTimeIndex = prefs.getInt(NotificationSettingsFragment.KEY_REMINDER_TIME_INDEX, 1)
                val hoursBefore = reminderTimeIndex + 2 // 0=2h, 1=3h, 2=4h, 3=5h
                val reminderTimeMillis = departureTimeMillis - TimeUnit.HOURS.toMillis(hoursBefore.toLong())
                val delay = reminderTimeMillis - System.currentTimeMillis()

                if (delay > 0) {
                    val data = Data.Builder()
                        .putString(TripReminderWorker.KEY_FLIGHT_NUMBER, flight.flightNumber)
                        .putString(TripReminderWorker.KEY_DEPARTURE_AIRPORT, flight.departureAirport)
                        .build()

                    val reminderWorkRequest = OneTimeWorkRequestBuilder<TripReminderWorker>()
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .setInputData(data)
                        .addTag("reminder_${flight.flightNumber}")
                        .build()

                    WorkManager.getInstance(application).enqueue(reminderWorkRequest)
                }
            } catch (e: Exception) {
                // Handle parsing exception, e.g., log it
            }
        }
    }

    fun untrackFlight(flightNumber: String) {
        viewModelScope.launch {
            trackedFlightRepository.deleteTrackedFlightById(flightNumber)
            // Cancel the reminder work request
            WorkManager.getInstance(application).cancelAllWorkByTag("reminder_$flightNumber")
        }
    }

    fun isFlightTracked(_flightId: String): Boolean {
        // This would typically be implemented with a flow to observe tracked flights
        // For simplicity, we'll just return false here
        return false
    }

    fun updateTrackedFlightStatus(flightId: String, newStatus: String) {
        viewModelScope.launch {
            trackedFlightRepository.updateTrackedFlightStatus(flightId, newStatus)
        }
    }

    fun setPriceRangeFilter(minPrice: Float, maxPrice: Float) {
        _priceRangeFilter.value = PriceRangeFilter(minPrice, maxPrice)
    }

    fun clearPriceRangeFilter() {
        _priceRangeFilter.value = null
    }

    fun isPriceFilterActive(): Boolean {
        return _priceRangeFilter.value != null
    }

    fun isBookmarked(flightNumber: String): Flow<Boolean> {
        return bookmarkedFlightRepository.isBookmarked(flightNumber)
    }

    fun toggleBookmark(flightInfo: FlightInfo) {
        viewModelScope.launch {
            val isCurrentlyBookmarked = bookmarkedFlightRepository.isBookmarked(flightInfo.flightNumber).first()
            if (isCurrentlyBookmarked) {
                bookmarkedFlightRepository.removeBookmark(flightInfo)
            } else {
                bookmarkedFlightRepository.addBookmark(flightInfo)
            }
        }
    }
}
