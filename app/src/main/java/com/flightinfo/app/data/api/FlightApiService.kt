package com.flightinfo.app.data.api

import com.flightinfo.app.data.model.FlightBooking
import com.flightinfo.app.data.model.FlightInfo
import com.flightinfo.app.data.model.FlightPriceInfo
import com.flightinfo.app.data.model.FlightSearchResponse
import com.flightinfo.app.data.model.FlightWeatherInfo
import com.flightinfo.app.data.model.MealPreference
import com.flightinfo.app.data.model.SeatMapResponse
import com.flightinfo.app.data.model.TravelSuggestionResponse
import com.flightinfo.app.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FlightApiService {
    /**
     * 搜索航班。
     * @param flightNumber 航班号
     * @param departureAirport 出发机场
     * @param arrivalAirport 到达机场
     * @param date 日期
     * @return 航班搜索结果
     */
    @GET("flights/search")
    suspend fun searchFlights(
        @Query("flight_number") flightNumber: String? = null,
        @Query("departure_airport") departureAirport: String? = null,
        @Query("arrival_airport") arrivalAirport: String? = null,
        @Query("date") date: String? = null,
    ): Response<FlightSearchResponse>

    /**
     * 获取实时航班信息。
     * @return 实时航班信息
     */
    @GET("flights/realtime")
    suspend fun getRealtimeFlights(): Response<FlightSearchResponse>

    /**
     * 获取航班详细信息。
     * @param flightNumber 航班号
     * @return 航班详细信息
     */
    @GET("flights/{flightNumber}")
    suspend fun getFlightDetails(
        @Path("flightNumber") flightNumber: String,
    ): Response<FlightInfo>

    /**
     * 获取旅行建议。
     * @param destination 目的地
     * @return 旅行建议
     */
    @GET("travel/suggestions")
    suspend fun getTravelSuggestions(
        @Query("destination") destination: String,
    ): Response<TravelSuggestionResponse>

    /**
     * 预订航班。
     * @param flightId 航班ID
     * @param bookingInfo 预订信息
     * @return 预订结果
     */
    @POST("flights/{flightId}/book")
    suspend fun bookFlight(
        @Path("flightId") flightId: String,
        @Body bookingInfo: FlightBooking,
    ): Response<Unit>

    /**
     * 获取航班座位图。
     * @param flightId 航班ID
     * @return 座位图信息
     */
    @GET("flights/{flightId}/seatmap")
    suspend fun getSeatMap(
        @Path("flightId") flightId: String,
    ): Response<SeatMapResponse>

    /**
     * 获取餐食选项。
     * @return 餐食选项列表
     */
    @GET("options/meals")
    suspend fun getMealOptions(): Response<Map<MealPreference, String>>

    /**
     * 获取保险选项。
     * @return 保险选项列表
     */
    @GET("options/insurance")
    suspend fun getInsuranceOptions(): Response<Map<String, Double>>

    /**
     * 获取行李费用信息。
     * @return 行李费用信息
     */
    @GET("options/baggage")
    suspend fun getBaggageFees(): Response<Map<String, Double>>

    /**
     * 获取指定地点的当前天气信息。
     * @param location 地点名称
     * @return 天气信息
     */
    @GET("weather/current")
    suspend fun getCurrentWeather(
        @Query("location") location: String,
    ): Response<WeatherResponse>

    /**
     * 获取指定地点的天气预报。
     * @param location 地点名称
     * @param days 预报天数
     * @return 天气预报信息
     */
    @GET("weather/forecast")
    suspend fun getWeatherForecast(
        @Query("location") location: String,
        @Query("days") days: Int = 5,
    ): Response<WeatherResponse>

    /**
     * 获取航班的天气影响信息。
     * @param flightNumber 航班号
     * @param departureAirport 出发机场
     * @param arrivalAirport 到达机场
     * @param date 日期
     * @return 航班天气信息
     */
    @GET("flights/weather")
    suspend fun getFlightWeather(
        @Query("flight_number") flightNumber: String,
        @Query("departure_airport") departureAirport: String,
        @Query("arrival_airport") arrivalAirport: String,
        @Query("date") date: String,
    ): Response<FlightWeatherInfo>

    /**
     * 获取航班价格信息。
     * @param flightId 航班ID
     * @return 航班价格信息
     */
    @GET("flights/{flightId}/price")
    suspend fun getFlightPrice(
        @Path("flightId") flightId: String,
    ): Response<FlightPriceInfo>

    companion object {
        const val BASE_URL = "https://api.aviationstack.com/v1/"

        // For demo purposes, we'll use mock data
        const val MOCK_BASE_URL = "https://jsonplaceholder.typicode.com/"

        // Weather API base URL
        const val WEATHER_BASE_URL = "https://api.weatherapi.com/v1/"
    }
}
