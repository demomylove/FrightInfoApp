package com.flightinfo.weather.data

// Nullable numeric wrappers preserve the difference between a missing value and a real zero.
data class SearchDto(val results: List<CityDto?>? = null, val error: Boolean? = null)
data class CityDto(
    val id: Long? = null, val name: String? = null, val latitude: Double? = null,
    val longitude: Double? = null, val country_code: String? = null, val feature_code: String? = null,
    val admin1: String? = null, val admin2: String? = null, val timezone: String? = null,
)
data class ForecastDto(
    val current: CurrentDto? = null, val current_units: CurrentUnits? = null,
    val daily: DailyDto? = null, val daily_units: DailyUnits? = null,
    val timezone: String? = null, val utc_offset_seconds: Int? = null,
    val latitude: Double? = null, val longitude: Double? = null, val error: Boolean? = null,
)
data class CurrentDto(val time: String? = null, val temperature_2m: Double? = null, val weather_code: Int? = null)
data class CurrentUnits(val temperature_2m: String? = null)
data class DailyDto(
    val time: List<String?>? = null, val weather_code: List<Int?>? = null,
    val temperature_2m_max: List<Double?>? = null, val temperature_2m_min: List<Double?>? = null,
)
data class DailyUnits(val temperature_2m_max: String? = null, val temperature_2m_min: String? = null)
