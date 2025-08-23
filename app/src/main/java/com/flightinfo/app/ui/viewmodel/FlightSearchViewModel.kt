package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.model.FlightSearchResponse
import com.flightinfo.app.data.model.TrackedFlight
import com.flightinfo.app.data.model.TravelSuggestionResponse
import com.flightinfo.app.data.repository.FlightRepository
import com.flightinfo.app.data.repository.TrackedFlightRepository
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightSearchViewModel @Inject constructor(
    private val repository: FlightRepository,
    private val trackedFlightRepository: TrackedFlightRepository,
) : ViewModel() {

    private val _searchResults = MutableStateFlow<Resource<FlightSearchResponse>>(Resource.Loading())
    val searchResults: StateFlow<Resource<FlightSearchResponse>> = _searchResults.asStateFlow()

    private val _realtimeFlights = MutableStateFlow<Resource<FlightSearchResponse>>(Resource.Loading())
    val realtimeFlights: StateFlow<Resource<FlightSearchResponse>> = _realtimeFlights.asStateFlow()

    private val _travelSuggestions = MutableStateFlow<Resource<TravelSuggestionResponse>>(Resource.Loading())
    val travelSuggestions: StateFlow<Resource<TravelSuggestionResponse>> = _travelSuggestions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

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

    fun trackFlight(flightId: String, flightNumber: String, currentStatus: String) {
        viewModelScope.launch {
            val trackedFlight = TrackedFlight(
                flightId = flightId,
                flightNumber = flightNumber,
                lastStatus = currentStatus,
                lastUpdated = System.currentTimeMillis(),
                notificationEnabled = true,
            )
            trackedFlightRepository.insertTrackedFlight(trackedFlight)
        }
    }

    fun untrackFlight(flightId: String) {
        viewModelScope.launch {
            trackedFlightRepository.deleteTrackedFlightById(flightId)
        }
    }

    fun isFlightTracked(flightId: String): Boolean {
        // This would typically be implemented with a flow to observe tracked flights
        // For simplicity, we'll just return false here
        return false
    }

    fun updateTrackedFlightStatus(flightId: String, newStatus: String) {
        viewModelScope.launch {
            trackedFlightRepository.updateTrackedFlightStatus(flightId, newStatus)
        }
    }
}
