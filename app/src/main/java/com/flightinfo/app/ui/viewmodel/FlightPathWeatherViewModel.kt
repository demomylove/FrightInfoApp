package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.model.FlightPathWeatherResponse
import com.flightinfo.app.data.repository.FlightPathWeatherRepository
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 航路天气 ViewModel
 */
@HiltViewModel
class FlightPathWeatherViewModel @Inject constructor(
    private val repository: FlightPathWeatherRepository,
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val weatherState: StateFlow<WeatherUiState> = _weatherState.asStateFlow()

    private val _turbulenceState = MutableStateFlow<TurbulenceUiState>(TurbulenceUiState.Idle)
    val turbulenceState: StateFlow<TurbulenceUiState> = _turbulenceState.asStateFlow()

    /**
     * 加载航路天气数据
     */
    fun loadFlightPathWeather(
        flightId: String,
        departureAirport: String,
        arrivalAirport: String,
        date: String,
        includeAlternates: Boolean = true,
        includeOptimalWindows: Boolean = true,
    ) {
        viewModelScope.launch {
            repository.getFlightPathWeather(
                flightId = flightId,
                departureAirport = departureAirport,
                arrivalAirport = arrivalAirport,
                date = date,
                includeAlternates = includeAlternates,
                includeOptimalWindows = includeOptimalWindows,
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _weatherState.value = WeatherUiState.Loading
                    }
                    is Resource.Success -> {
                        _weatherState.value = WeatherUiState.Success(resource.data)
                    }
                    is Resource.Error -> {
                        _weatherState.value = WeatherUiState.Error(
                            resource.message ?: "加载航路天气失败",
                        )
                    }
                    is Resource.Idle -> {
                        _weatherState.value = WeatherUiState.Idle
                    }
                }
            }
        }
    }

    /**
     * 加载颠簸预测数据
     */
    fun loadTurbulenceForecast(
        latitude: Double,
        longitude: Double,
        altitude: Int,
        radius: Int = 50,
    ) {
        viewModelScope.launch {
            repository.getTurbulenceForecast(
                latitude = latitude,
                longitude = longitude,
                altitude = altitude,
                radius = radius,
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _turbulenceState.value = TurbulenceUiState.Loading
                    }
                    is Resource.Success -> {
                        _turbulenceState.value = TurbulenceUiState.Success(resource.data)
                    }
                    is Resource.Error -> {
                        _turbulenceState.value = TurbulenceUiState.Error(
                            resource.message ?: "加载颠簸预测失败",
                        )
                    }
                    is Resource.Idle -> {
                        _turbulenceState.value = TurbulenceUiState.Idle
                    }
                }
            }
        }
    }

    /**
     * 加载天气雷达数据
     */
    fun loadWeatherRadar(
        departureAirport: String,
        arrivalAirport: String,
        flightLevel: Int = 350,
    ) {
        viewModelScope.launch {
            repository.getWeatherRadar(
                departureAirport = departureAirport,
                arrivalAirport = arrivalAirport,
                flightLevel = flightLevel,
            ).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _weatherState.value = WeatherUiState.Loading
                    }
                    is Resource.Success -> {
                        _weatherState.value = WeatherUiState.Success(resource.data)
                    }
                    is Resource.Error -> {
                        _weatherState.value = WeatherUiState.Error(
                            resource.message ?: "加载雷达数据失败",
                        )
                    }
                    is Resource.Idle -> {
                        _weatherState.value = WeatherUiState.Idle
                    }
                }
            }
        }
    }

    /**
     * 重置状态
     */
    fun resetState() {
        _weatherState.value = WeatherUiState.Idle
        _turbulenceState.value = TurbulenceUiState.Idle
    }
}

/**
 * 天气 UI 状态
 */
sealed class WeatherUiState {
    object Idle : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val data: FlightPathWeatherResponse?) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

/**
 * 颠簸预测 UI 状态
 */
sealed class TurbulenceUiState {
    object Idle : TurbulenceUiState()
    object Loading : TurbulenceUiState()
    data class Success(val data: FlightPathWeatherResponse?) : TurbulenceUiState()
    data class Error(val message: String) : TurbulenceUiState()
}
