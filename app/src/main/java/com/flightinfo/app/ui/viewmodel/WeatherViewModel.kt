package com.flightinfo.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flightinfo.app.data.model.FlightWeatherInfo
import com.flightinfo.app.data.model.WeatherResponse
import com.flightinfo.app.data.repository.WeatherRepository
import com.flightinfo.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private val _currentWeather = MutableStateFlow<Resource<WeatherResponse>>(Resource.Loading())
    val currentWeather: StateFlow<Resource<WeatherResponse>> = _currentWeather.asStateFlow()

    private val _weatherForecast = MutableStateFlow<Resource<WeatherResponse>>(Resource.Loading())
    val weatherForecast: StateFlow<Resource<WeatherResponse>> = _weatherForecast.asStateFlow()

    private val _flightWeather = MutableStateFlow<Resource<FlightWeatherInfo>>(Resource.Loading())
    val flightWeather: StateFlow<Resource<FlightWeatherInfo>> = _flightWeather.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * 获取指定地点的当前天气信息。
     * @param location 地点名称
     */
    fun getCurrentWeather(location: String) {
        viewModelScope.launch {
            _isLoading.value = true
            weatherRepository.getCurrentWeather(location)
                .collect { result ->
                    _currentWeather.value = result
                    _isLoading.value = false
                }
        }
    }

    /**
     * 获取指定地点的天气预报。
     * @param location 地点名称
     * @param days 预报天数
     */
    fun getWeatherForecast(location: String, days: Int = 5) {
        viewModelScope.launch {
            _isLoading.value = true
            weatherRepository.getWeatherForecast(location, days)
                .collect { result ->
                    _weatherForecast.value = result
                    _isLoading.value = false
                }
        }
    }

    /**
     * 获取航班的天气影响信息。
     * @param flightNumber 航班号
     * @param departureAirport 出发机场
     * @param arrivalAirport 到达机场
     * @param date 日期
     */
    fun getFlightWeather(
        flightNumber: String,
        departureAirport: String,
        arrivalAirport: String,
        date: String,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            weatherRepository.getFlightWeather(flightNumber, departureAirport, arrivalAirport, date)
                .collect { result ->
                    _flightWeather.value = result
                    _isLoading.value = false
                }
        }
    }

    /**
     * 清除天气数据。
     */
    fun clearWeatherData() {
        _currentWeather.value = Resource.Loading()
        _weatherForecast.value = Resource.Loading()
        _flightWeather.value = Resource.Loading()
    }
}
