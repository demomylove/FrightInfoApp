package com.flightinfo.app.data.repository

import com.flightinfo.app.data.api.FlightApiService
import com.flightinfo.app.data.model.FlightWeatherInfo
import com.flightinfo.app.data.model.WeatherResponse
import com.flightinfo.app.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val apiService: FlightApiService,
) {

    /**
     * 获取指定地点的当前天气信息。
     * @param location 地点名称
     * @return 返回一个包含天气信息的 Flow。
     */
    fun getCurrentWeather(location: String): Flow<Resource<WeatherResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getCurrentWeather(location)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.message}"))
        }
    }

    /**
     * 获取指定地点的天气预报。
     * @param location 地点名称
     * @param days 预报天数
     * @return 返回一个包含天气预报信息的 Flow。
     */
    fun getWeatherForecast(location: String, days: Int = 5): Flow<Resource<WeatherResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getWeatherForecast(location, days)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.message}"))
        }
    }

    /**
     * 获取航班的天气影响信息。
     * @param flightNumber 航班号
     * @param departureAirport 出发机场
     * @param arrivalAirport 到达机场
     * @param date 日期
     * @return 返回一个包含航班天气信息的 Flow。
     */
    fun getFlightWeather(
        flightNumber: String,
        departureAirport: String,
        arrivalAirport: String,
        date: String,
    ): Flow<Resource<FlightWeatherInfo>> = flow {
        try {
            emit(Resource.Loading())
            val response = apiService.getFlightWeather(flightNumber, departureAirport, arrivalAirport, date)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Network error: ${e.message}"))
        }
    }
}
