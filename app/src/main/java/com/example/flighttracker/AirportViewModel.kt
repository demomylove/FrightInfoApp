package com.example.flighttracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AirportViewModel : ViewModel() {
    private val repository = AirportRepositoryImpl()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Airport>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedAirport = MutableStateFlow<Airport?>(null)
    val selectedAirport = _selectedAirport.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        searchAirports(query)
    }

    private fun searchAirports(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val results = repository.searchAirports(query)
                _searchResults.value = results
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectAirport(airport: Airport) {
        _selectedAirport.value = airport
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun clearSelection() {
        _selectedAirport.value = null
    }

    fun getAirportByCode(code: String): Airport? {
        return try {
            var result: Airport? = null
            viewModelScope.launch {
                result = repository.getAirportByCode(code)
            }
            result
        } catch (e: Exception) {
            null
        }
    }
}
