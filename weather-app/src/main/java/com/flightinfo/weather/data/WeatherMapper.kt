package com.flightinfo.weather.data

import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

class WeatherMapper(private val clock: Clock) {
    fun cities(dto: SearchDto): List<City> {
        if (dto.error == true) throw WeatherFailure(WeatherFailure.Kind.SERVICE)
        var damaged = false
        val result = dto.results.orEmpty().mapNotNull { item ->
            if (item == null) { damaged = true; return@mapNotNull null }
            if (item.country_code == null || item.feature_code == null) {
                damaged = true; return@mapNotNull null
            }
            if (item.country_code != "CN" || !item.feature_code.startsWith("PPL")) return@mapNotNull null
            val lat = item.latitude
            val lon = item.longitude
            if (item.id == null || item.name.isNullOrBlank() || lat == null || lon == null ||
                !lat.isFinite() || !lon.isFinite() || lat !in -90.0..90.0 || lon !in -180.0..180.0
            ) { damaged = true; return@mapNotNull null }
            City(item.id, item.name, listOfNotNull(item.admin1, item.admin2).filter { it.isNotBlank() }, lat, lon, item.timezone)
        }.distinctBy { it.id }
        if (result.isEmpty() && damaged) throw WeatherFailure(WeatherFailure.Kind.FORMAT)
        return result
    }

    fun snapshot(city: City, dto: ForecastDto): WeatherSnapshot {
        if (dto.error == true) throw WeatherFailure(WeatherFailure.Kind.SERVICE)
        if (dto.timezone != "Asia/Shanghai" || dto.utc_offset_seconds != 28800) throw WeatherFailure(WeatherFailure.Kind.FORMAT)
        val date = today(clock)
        val daily = dto.daily
        val dates = daily?.time.orEmpty().map { raw -> raw?.let { runCatching { LocalDate.parse(it) }.getOrNull() } }
        val duplicates = dates.filterNotNull().groupingBy { it }.eachCount().filterValues { it > 1 }.keys
        val days = dates.mapIndexedNotNull { i, d ->
            if (d == null || d in duplicates || d !in targetDates(date)) return@mapIndexedNotNull null
            var high = temperature(daily?.temperature_2m_max?.getOrNull(i), dto.daily_units?.temperature_2m_max)
            var low = temperature(daily?.temperature_2m_min?.getOrNull(i), dto.daily_units?.temperature_2m_min)
            if (high != null && low != null && high < low) { high = null; low = null }
            DayWeather(d, daily?.weather_code?.getOrNull(i), high, low)
        }.sortedBy { it.date }
        return WeatherSnapshot(
            city, clock.instant(), date,
            dto.current?.time?.let { runCatching { LocalDateTime.parse(it) }.getOrNull() },
            temperature(dto.current?.temperature_2m, dto.current_units?.temperature_2m), dto.current?.weather_code,
            days, dto.latitude, dto.longitude, dates.any { it == null } || duplicates.isNotEmpty(),
        )
    }

    private fun temperature(value: Double?, unit: String?) = value?.takeIf { it.isFinite() && unit == "°C" }
}
