package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.model.FlightInfo
import com.flightinfo.app.data.model.FlightRecommendation
import com.flightinfo.app.data.model.RecommendationAnalytics
import com.flightinfo.app.data.model.RecommendationType
import com.flightinfo.app.data.model.UserBehaviorStats
import com.flightinfo.app.data.model.UserPreferences
import com.flightinfo.app.data.repository.RecommendationRepository
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendationViewModel @Inject constructor(
    private val recommendationRepository: RecommendationRepository,
) : ViewModel() {

    private val _recommendations = MutableStateFlow<Resource<List<FlightRecommendation>>>(Resource.Loading())
    val recommendations: StateFlow<Resource<List<FlightRecommendation>>> = _recommendations

    private val _userStats = MutableStateFlow<Resource<UserBehaviorStats>>(Resource.Loading())
    val userStats: StateFlow<Resource<UserBehaviorStats>> = _userStats

    private val _analytics = MutableStateFlow<Resource<RecommendationAnalytics>>(Resource.Loading())
    val analytics: StateFlow<Resource<RecommendationAnalytics>> = _analytics

    private val _userPreferences = MutableStateFlow<Resource<UserPreferences>>(Resource.Loading())
    val userPreferences: StateFlow<Resource<UserPreferences>> = _userPreferences

    private val _refreshing = MutableStateFlow(false)
    val refreshing: StateFlow<Boolean> = _refreshing

    init {
        loadRecommendations()
        loadUserStats()
        loadUserPreferences()
    }

    fun loadRecommendations() {
        viewModelScope.launch {
            recommendationRepository.getRecommendations()
                .collect { result ->
                    _recommendations.value = result
                }
        }
    }

    fun loadUserStats() {
        viewModelScope.launch {
            recommendationRepository.getUserBehaviorStats()
                .collect { result ->
                    _userStats.value = result
                }
        }
    }

    fun loadAnalytics() {
        viewModelScope.launch {
            recommendationRepository.getRecommendationAnalytics()
                .collect { result ->
                    _analytics.value = result
                }
        }
    }

    fun loadUserPreferences() {
        viewModelScope.launch {
            recommendationRepository.getUserPreferences()
                .collect { result ->
                    _userPreferences.value = result
                }
        }
    }

    fun refreshRecommendations() {
        viewModelScope.launch {
            _refreshing.value = true
            try {
                loadRecommendations()
                loadUserStats()
                loadAnalytics()
            } finally {
                _refreshing.value = false
            }
        }
    }

    fun trackRecommendationClick(recommendation: FlightRecommendation) {
        viewModelScope.launch {
            recommendationRepository.trackRecommendationInteraction(recommendation, "click")
        }
    }

    fun trackFlightAction(action: String, flightInfo: FlightInfo) {
        viewModelScope.launch {
            recommendationRepository.trackUserAction(action, flightInfo)
        }
    }

    fun updatePreferences(preferences: UserPreferences) {
        viewModelScope.launch {
            recommendationRepository.updateUserPreferences(preferences)
                .collect { result ->
                    if (result is Resource.Success) {
                        loadUserPreferences()
                        refreshRecommendations() // 更新偏好后刷新推荐
                    }
                }
        }
    }

    fun clearOldData() {
        viewModelScope.launch {
            recommendationRepository.clearOldData()
                .collect { result ->
                    if (result is Resource.Success) {
                        loadUserStats()
                        loadAnalytics()
                    }
                }
        }
    }

    fun getRecommendationStats(): Flow<Map<String, Int>> {
        return _recommendations.map { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.groupBy { it.recommendationType.name }?.mapValues { it.value.size } ?: emptyMap()
                }
                else -> emptyMap()
            }
        }
    }

    fun getTopAirlines(): Flow<List<String>> {
        return _userStats.map { resource ->
            when (resource) {
                is Resource.Success -> resource.data?.topAirlines ?: emptyList()
                else -> emptyList()
            }
        }
    }

    fun getTopAirports(): Flow<List<String>> {
        return _userStats.map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val allAirports = mutableListOf<String>()
                    resource.data?.topDepartureAirports?.let { allAirports.addAll(it) }
                    resource.data?.topArrivalAirports?.let { allAirports.addAll(it) }
                    allAirports.distinct().take(10)
                }
                else -> emptyList()
            }
        }
    }

    fun getAverageConfidenceScore(): Flow<Double> {
        return _recommendations.map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val scores = resource.data?.map { it.confidenceScore } ?: emptyList()
                    if (scores.isNotEmpty()) scores.average() else 0.0
                }
                else -> 0.0
            }
        }
    }

    fun getRecommendationByType(type: RecommendationType): Flow<List<FlightRecommendation>> {
        return _recommendations.map { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.filter { it.recommendationType == type } ?: emptyList()
                }
                else -> emptyList()
            }
        }
    }

    fun searchRecommendations(query: String): Flow<List<FlightRecommendation>> {
        return _recommendations.map { resource ->
            when (resource) {
                is Resource.Success -> {
                    val lowercaseQuery = query.lowercase()
                    resource.data?.filter { recommendation ->
                        recommendation.flight.flightNumber.lowercase().contains(lowercaseQuery) ||
                            recommendation.flight.airline.lowercase().contains(lowercaseQuery) ||
                            recommendation.flight.departureAirport.lowercase().contains(lowercaseQuery) ||
                            recommendation.flight.arrivalAirport.lowercase().contains(lowercaseQuery)
                    } ?: emptyList()
                }
                else -> emptyList()
            }
        }
    }

    fun filterRecommendationsByPrice(minPrice: Double?, maxPrice: Double?): Flow<List<FlightRecommendation>> {
        return _recommendations.map { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.filter { recommendation ->
                        val price = recommendation.flight.price
                        when {
                            minPrice != null && maxPrice != null -> price != null && price in minPrice..maxPrice
                            minPrice != null -> price != null && price >= minPrice
                            maxPrice != null -> price != null && price <= maxPrice
                            else -> true
                        }
                    } ?: emptyList()
                }
                else -> emptyList()
            }
        }
    }

    fun filterRecommendationsByAirline(airlines: List<String>): Flow<List<FlightRecommendation>> {
        return _recommendations.map { resource ->
            when (resource) {
                is Resource.Success -> {
                    if (airlines.isEmpty()) {
                        resource.data ?: emptyList()
                    } else {
                        resource.data?.filter { recommendation ->
                            recommendation.flight.airline in airlines
                        } ?: emptyList()
                    }
                }
                else -> emptyList()
            }
        }
    }

    fun filterRecommendationsByAirport(airports: List<String>): Flow<List<FlightRecommendation>> {
        return _recommendations.map { resource ->
            when (resource) {
                is Resource.Success -> {
                    if (airports.isEmpty()) {
                        resource.data ?: emptyList()
                    } else {
                        resource.data?.filter { recommendation ->
                            recommendation.flight.departureAirport in airports ||
                                recommendation.flight.arrivalAirport in airports
                        } ?: emptyList()
                    }
                }
                else -> emptyList()
            }
        }
    }
}
