package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.model.FlightSchedule
import com.flightinfo.app.data.model.OfflineAirportInfo
import com.flightinfo.app.data.repository.FlightScheduleRepository
import com.flightinfo.app.service.DownloadStats
import com.flightinfo.app.service.OfflineScheduleManager
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineScheduleViewModel @Inject constructor(
    private val flightScheduleRepository: FlightScheduleRepository,
    private val offlineScheduleManager: OfflineScheduleManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow<OfflineScheduleUiState>(OfflineScheduleUiState.Loading)
    val uiState: StateFlow<OfflineScheduleUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedDeparture = MutableStateFlow<String?>(null)
    val selectedDeparture: StateFlow<String?> = _selectedDeparture.asStateFlow()

    private val _selectedArrival = MutableStateFlow<String?>(null)
    val selectedArrival: StateFlow<String?> = _selectedArrival.asStateFlow()

    private val _selectedAirline = MutableStateFlow<String?>(null)
    val selectedAirline: StateFlow<String?> = _selectedAirline.asStateFlow()

    private val _selectedDayOfWeek = MutableStateFlow<Int?>(null)
    val selectedDayOfWeek: StateFlow<Int?> = _selectedDayOfWeek.asStateFlow()

    init {
        loadOfflineData()
    }

    private fun loadOfflineData() {
        viewModelScope.launch {
            offlineScheduleManager.getOfflineDataStatus()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.value = OfflineScheduleUiState.Loading
                        }
                        is Resource.Success -> {
                            val status = resource.data
                            if (status?.hasFlightSchedules == true) {
                                loadFlightSchedules()
                            } else {
                                _uiState.value = OfflineScheduleUiState.NoData
                            }
                        }
                        is Resource.Error -> {
                            _uiState.value = OfflineScheduleUiState.Error(resource.message ?: "Unknown error")
                        }
                    }
                }
        }
    }

    private fun loadFlightSchedules() {
        viewModelScope.launch {
            combine(
                searchQuery,
                selectedDeparture,
                selectedArrival,
                selectedAirline,
                selectedDayOfWeek,
            ) { query, departure, arrival, airline, dayOfWeek ->
                flightScheduleRepository.searchFlights(
                    departureCode = departure,
                    arrivalCode = arrival,
                    airline = airline,
                    dayOfWeek = dayOfWeek,
                    query = if (query.isNotBlank()) query else null,
                )
            }.flatMapLatest { it }
                .collect { schedules ->
                    _uiState.value = OfflineScheduleUiState.Success(
                        schedules = schedules,
                        airports = flightScheduleRepository.getAllAirports().first(),
                    )
                }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setDepartureAirport(code: String?) {
        _selectedDeparture.value = code
    }

    fun setArrivalAirport(code: String?) {
        _selectedArrival.value = code
    }

    fun setAirline(airline: String?) {
        _selectedAirline.value = airline
    }

    fun setDayOfWeek(day: Int?) {
        _selectedDayOfWeek.value = day
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedDeparture.value = null
        _selectedArrival.value = null
        _selectedAirline.value = null
        _selectedDayOfWeek.value = null
    }

    fun downloadSchedules() {
        viewModelScope.launch {
            offlineScheduleManager.downloadAndCacheSchedules()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.value = OfflineScheduleUiState.Downloading
                        }
                        is Resource.Success -> {
                            loadOfflineData()
                        }
                        is Resource.Error -> {
                            _uiState.value = OfflineScheduleUiState.Error(resource.message ?: "Unknown error")
                        }
                    }
                }
        }
    }

    fun clearCachedData() {
        viewModelScope.launch {
            offlineScheduleManager.clearAllCachedData()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.value = OfflineScheduleUiState.Loading
                        }
                        is Resource.Success -> {
                            _uiState.value = OfflineScheduleUiState.NoData
                        }
                        is Resource.Error -> {
                            _uiState.value = OfflineScheduleUiState.Error(resource.message ?: "Unknown error")
                        }
                    }
                }
        }
    }

    fun refreshData() {
        loadOfflineData()
    }

    fun getDownloadStats() {
        viewModelScope.launch {
            offlineScheduleManager.getDownloadStats()
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            // Update UI state if needed
                        }
                        is Resource.Success -> {
                            // Update download stats in UI state
                            val currentState = _uiState.value
                            if (currentState is OfflineScheduleUiState.Success) {
                                _uiState.value = currentState.copy(
                                    downloadStats = resource.data,
                                )
                            }
                        }
                        is Resource.Error -> {
                            // Handle error
                        }
                    }
                }
        }
    }

    fun toggleAutoRefresh(enabled: Boolean) {
        offlineScheduleManager.setAutoRefreshEnabled(enabled)
    }

    fun isAutoRefreshEnabled(): Boolean {
        return offlineScheduleManager.isAutoRefreshEnabled()
    }
}

sealed class OfflineScheduleUiState {
    object Loading : OfflineScheduleUiState()
    object NoData : OfflineScheduleUiState()
    object Downloading : OfflineScheduleUiState()
    data class Success(
        val schedules: List<FlightSchedule>,
        val airports: List<OfflineAirportInfo>,
        val downloadStats: DownloadStats? = null,
    ) : OfflineScheduleUiState()
    data class Error(val message: String) : OfflineScheduleUiState()
}
