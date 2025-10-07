package com.flightinfo.app.data.api

import com.flightinfo.app.data.model.FlightPathWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 航路天气 API 服务（NOAA/NASA 数据集成）
 */
interface FlightPathWeatherApiService {
    /**
     * 获取航路天气与颠簸预警信息
     * @param flightId 航班ID
     * @param departureAirport 出发机场代码
     * @param arrivalAirport 到达机场代码
     * @param date 航班日期 (YYYY-MM-DD)
     * @param includeAlternates 是否包含备降机场建议
     * @param includeOptimalWindows 是否包含最佳飞行时段
     * @return 航路天气响应
     */
    @GET("flight-path/weather")
    suspend fun getFlightPathWeather(
        @Query("flight_id") flightId: String,
        @Query("departure") departureAirport: String,
        @Query("arrival") arrivalAirport: String,
        @Query("date") date: String,
        @Query("include_alternates") includeAlternates: Boolean = true,
        @Query("include_optimal_windows") includeOptimalWindows: Boolean = true,
    ): Response<FlightPathWeatherResponse>

    /**
     * 获取特定航路段的颠簸预测
     * @param latitude 纬度
     * @param longitude 经度
     * @param altitude 高度（英尺）
     * @param radius 搜索半径（海里）
     * @return 航路天气响应
     */
    @GET("turbulence/forecast")
    suspend fun getTurbulenceForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("altitude") altitude: Int,
        @Query("radius") radius: Int = 50,
    ): Response<FlightPathWeatherResponse>

    /**
     * 获取航路天气雷达数据
     * @param departureAirport 出发机场代码
     * @param arrivalAirport 到达机场代码
     * @param flightLevel 飞行高度层（FL，如 FL350 = 35000英尺）
     * @return 航路天气响应
     */
    @GET("weather/radar")
    suspend fun getWeatherRadar(
        @Query("departure") departureAirport: String,
        @Query("arrival") arrivalAirport: String,
        @Query("flight_level") flightLevel: Int,
    ): Response<FlightPathWeatherResponse>

    companion object {
        // NOAA Aviation Weather Center API
        const val NOAA_BASE_URL = "https://aviationweather.gov/api/data/"

        // NASA Turbulence Forecast API (模拟)
        const val NASA_BASE_URL = "https://turbulence.nasa.gov/api/v1/"

        // Mock endpoint for development
        const val MOCK_BASE_URL = "https://api.flightinfo.mock/v1/"
    }
}
