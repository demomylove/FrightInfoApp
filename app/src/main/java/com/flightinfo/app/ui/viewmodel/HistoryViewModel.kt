package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.repository.HistoricalFlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: HistoricalFlightRepository,
) : ViewModel() {

    val historicalFlights = repository.getHistoricalFlights().asLiveData()

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}
