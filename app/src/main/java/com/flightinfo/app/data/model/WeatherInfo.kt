package com.flightinfo.app.data.model

import com.google.gson.annotations.SerializedName

data class WeatherInfo(
    @SerializedName("location")
    val location: String,

    @SerializedName("temperature")
    val temperature: Double,

    @SerializedName("condition")
    val condition: String,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("wind_speed")
    val windSpeed: Double,

    @SerializedName("wind_direction")
    val windDirection: String,

    @SerializedName("visibility")
    val visibility: Double,

    @SerializedName("pressure")
    val pressure: Double,

    @SerializedName("forecast")
    val forecast: List<DailyForecast>,
)

data class DailyForecast(
    @SerializedName("date")
    val date: String,

    @SerializedName("max_temp")
    val maxTemp: Double,

    @SerializedName("min_temp")
    val minTemp: Double,

    @SerializedName("condition")
    val condition: String,

    @SerializedName("precipitation")
    val precipitation: Double,

    @SerializedName("wind_speed")
    val windSpeed: Double,

    @SerializedName("wind_direction")
    val windDirection: String,

    @SerializedName("humidity")
    val humidity: Int,

    @SerializedName("uv_index")
    val uvIndex: Int,
)

data class WeatherResponse(
    @SerializedName("current")
    val current: WeatherInfo,

    @SerializedName("location")
    val location: LocationInfo,
)

data class LocationInfo(
    @SerializedName("name")
    val name: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("region")
    val region: String,

    @SerializedName("lat")
    val lat: Double,

    @SerializedName("lon")
    val lon: Double,

    @SerializedName("timezone_id")
    val timezoneId: String,
)

data class FlightWeatherInfo(
    @SerializedName("flight_number")
    val flightNumber: String,

    @SerializedName("departure_weather")
    val departureWeather: WeatherInfo,

    @SerializedName("arrival_weather")
    val arrivalWeather: WeatherInfo,

    @SerializedName("weather_impact")
    val weatherImpact: WeatherImpact,
)

data class WeatherImpact(
    @SerializedName("delay_probability")
    val delayProbability: Double,

    @SerializedName("delay_minutes")
    val delayMinutes: Int,

    @SerializedName("cancellation_probability")
    val cancellationProbability: Double,

    @SerializedName("recommendation")
    val recommendation: String,
)
