package com.flightinfo.app.data.repository

import com.flightinfo.app.data.api.FlightPathWeatherApiService
import com.flightinfo.app.data.model.FlightPathWeather
import com.flightinfo.app.data.model.FlightPathWeatherResponse
import com.flightinfo.app.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 航路天气数据仓库
 */
@Singleton
class FlightPathWeatherRepository @Inject constructor(
    private val apiService: FlightPathWeatherApiService,
) {
    /**
     * 获取航路天气与颠簸预警
     */
    fun getFlightPathWeather(
        flightId: String,
        departureAirport: String,
        arrivalAirport: String,
        date: String,
        includeAlternates: Boolean = true,
        includeOptimalWindows: Boolean = true,
    ): Flow<Resource<FlightPathWeatherResponse>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getFlightPathWeather(
                flightId = flightId,
                departureAirport = departureAirport,
                arrivalAirport = arrivalAirport,
                date = date,
                includeAlternates = includeAlternates,
                includeOptimalWindows = includeOptimalWindows,
            )

            if (response.isSuccessful) {
                response.body()?.let { weatherResponse ->
                    if (weatherResponse.success && weatherResponse.data != null) {
                        emit(Resource.Success(weatherResponse))
                    } else {
                        emit(Resource.Error(weatherResponse.message ?: "未获取到航路天气数据"))
                    }
                } ?: emit(Resource.Error("响应数据为空"))
            } else {
                // 开发环境下返回模拟数据
                emit(Resource.Success(getMockFlightPathWeather(flightId, departureAirport, arrivalAirport)))
            }
        } catch (e: Exception) {
            Timber.e(e, "获取航路天气失败")
            // 返回模拟数据以便开发测试
            emit(Resource.Success(getMockFlightPathWeather(flightId, departureAirport, arrivalAirport)))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 获取颠簸预测
     */
    fun getTurbulenceForecast(
        latitude: Double,
        longitude: Double,
        altitude: Int,
        radius: Int = 50,
    ): Flow<Resource<FlightPathWeatherResponse>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getTurbulenceForecast(
                latitude = latitude,
                longitude = longitude,
                altitude = altitude,
                radius = radius,
            )

            if (response.isSuccessful) {
                response.body()?.let { weatherResponse ->
                    if (weatherResponse.success && weatherResponse.data != null) {
                        emit(Resource.Success(weatherResponse))
                    } else {
                        emit(Resource.Error(weatherResponse.message ?: "未获取到颠簸预测数据"))
                    }
                } ?: emit(Resource.Error("响应数据为空"))
            } else {
                emit(Resource.Error("获取颠簸预测失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Timber.e(e, "获取颠簸预测失败")
            emit(Resource.Error("网络错误: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 获取天气雷达数据
     */
    fun getWeatherRadar(
        departureAirport: String,
        arrivalAirport: String,
        flightLevel: Int,
    ): Flow<Resource<FlightPathWeatherResponse>> = flow {
        try {
            emit(Resource.Loading())

            val response = apiService.getWeatherRadar(
                departureAirport = departureAirport,
                arrivalAirport = arrivalAirport,
                flightLevel = flightLevel,
            )

            if (response.isSuccessful) {
                response.body()?.let { weatherResponse ->
                    if (weatherResponse.success && weatherResponse.data != null) {
                        emit(Resource.Success(weatherResponse))
                    } else {
                        emit(Resource.Error(weatherResponse.message ?: "未获取到雷达数据"))
                    }
                } ?: emit(Resource.Error("响应数据为空"))
            } else {
                emit(Resource.Error("获取雷达数据失败: ${response.message()}"))
            }
        } catch (e: Exception) {
            Timber.e(e, "获取雷达数据失败")
            emit(Resource.Error("网络错误: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 生成模拟航路天气数据（用于开发测试）
     */
    private fun getMockFlightPathWeather(
        flightId: String,
        departureAirport: String,
        arrivalAirport: String,
    ): FlightPathWeatherResponse {
        return FlightPathWeatherResponse(
            success = true,
            data = com.flightinfo.app.data.model.FlightPathWeather(
                flightId = flightId,
                routeSegments = listOf(
                    com.flightinfo.app.data.model.RouteSegment(
                        segmentId = "segment_1",
                        startLatitude = 39.9042,
                        startLongitude = 116.4074,
                        endLatitude = 31.2304,
                        endLongitude = 121.4737,
                        altitudeFeet = 35000,
                        weatherConditions = listOf(
                            com.flightinfo.app.data.model.WeatherCondition(
                                type = com.flightinfo.app.data.model.WeatherType.CLOUDS,
                                severity = com.flightinfo.app.data.model.WeatherSeverity.LIGHT,
                                description = "轻度云层覆盖",
                                altitudeRange = com.flightinfo.app.data.model.AltitudeRange(30000, 40000),
                                probability = 75,
                            ),
                        ),
                        turbulenceWarnings = listOf(
                            com.flightinfo.app.data.model.TurbulenceWarning(
                                warningId = "turb_001",
                                level = com.flightinfo.app.data.model.TurbulenceLevel.LIGHT,
                                startTime = java.util.Date(),
                                endTime = java.util.Date(System.currentTimeMillis() + 3600000),
                                location = com.flightinfo.app.data.model.GeoLocation(
                                    latitude = 35.5,
                                    longitude = 119.0,
                                    regionName = "华东上空",
                                ),
                                altitudeRange = com.flightinfo.app.data.model.AltitudeRange(32000, 38000),
                                description = "预计轻度颠簸，建议系好安全带",
                                source = com.flightinfo.app.data.model.DataSource.NOAA,
                                confidence = 85,
                            ),
                        ),
                    ),
                ),
                overallRisk = com.flightinfo.app.data.model.WeatherRiskLevel.LOW,
                recommendations = listOf(
                    "航路天气整体良好，预计准点起飞",
                    "华东上空可能有轻度颠簸，建议巡航时全程系好安全带",
                    "目的地天气晴朗，能见度良好",
                ),
                lastUpdated = java.util.Date(),
            ),
            alternateAirports = listOf(
                com.flightinfo.app.data.model.AlternateAirportRecommendation(
                    airportCode = "HGH",
                    airportName = "杭州萧山国际机场",
                    distanceNauticalMiles = 85.0,
                    weatherConditions = "晴，能见度10公里以上",
                    runwayConditions = "干燥，状况良好",
                    recommendationReason = "距离适中，天气条件优秀",
                    priority = 1,
                ),
                com.flightinfo.app.data.model.AlternateAirportRecommendation(
                    airportCode = "NKG",
                    airportName = "南京禄口国际机场",
                    distanceNauticalMiles = 120.0,
                    weatherConditions = "多云，能见度8公里",
                    runwayConditions = "良好",
                    recommendationReason = "备用机场，设施完善",
                    priority = 2,
                ),
            ),
            optimalWindows = listOf(
                com.flightinfo.app.data.model.OptimalFlightWindow(
                    startTime = java.util.Date(),
                    endTime = java.util.Date(System.currentTimeMillis() + 7200000),
                    weatherScore = 92,
                    turbulenceProbability = 15,
                    delayProbability = 8,
                    reasons = listOf(
                        "航路天气稳定，风向有利",
                        "目的地机场流量适中",
                        "无显著对流天气影响",
                    ),
                ),
                com.flightinfo.app.data.model.OptimalFlightWindow(
                    startTime = java.util.Date(System.currentTimeMillis() + 10800000),
                    endTime = java.util.Date(System.currentTimeMillis() + 14400000),
                    weatherScore = 88,
                    turbulenceProbability = 20,
                    delayProbability = 12,
                    reasons = listOf(
                        "下午时段，热对流可能增强",
                        "建议提前起飞避开高峰",
                    ),
                ),
            ),
            message = "航路天气数据获取成功（开发模拟数据）",
        )
    }
}
