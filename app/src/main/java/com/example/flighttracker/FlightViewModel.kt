package com.example.flighttracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlightViewModel : ViewModel() {
    private val repository = FlightRepositoryImpl()

    private val _flightInfo = MutableStateFlow<String?>(null)
    val flightInfo: StateFlow<String?> = _flightInfo

    fun searchFlight(flightNumber: String) {
        viewModelScope.launch {
            val result = repository.getFlightInfo(flightNumber)
            _flightInfo.value = result
        }
    }
}
