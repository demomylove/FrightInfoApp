package com.flightinfo.weather

import com.flightinfo.weather.data.*
import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class WeatherMapperTest {
    private val clock = Clock.fixed(Instant.parse("2026-12-31T15:59:00Z"), ZoneId.of("America/New_York"))
    private val mapper = WeatherMapper(clock)
    private val city = City(1, "北京", listOf("北京市"), 39.9, 116.4, "Asia/Shanghai")
    private fun valid(): ForecastDto {
        val dates = (0L..7).map { today(clock).plusDays(it).toString() }
        return ForecastDto(CurrentDto("2026-12-31T23:45", 0.0, 0), CurrentUnits("°C"),
            DailyDto(dates, List(8) { 1 }, List(8) { 10.0 }, List(8) { 0.0 }), DailyUnits("°C", "°C"),
            "Asia/Shanghai", 28800, 40.0, 116.5)
    }
    @Test fun tomorrowThroughSeventhDayAcrossYearUsesBeijingRegardlessOfDeviceZone() {
        val snapshot = mapper.snapshot(city, valid())
        assertTrue(snapshot.complete)
        assertEquals("2027-01-01", snapshot.days.first().date.toString())
        assertEquals("2027-01-07", snapshot.days.last().date.toString())
        assertEquals(city, snapshot.city)
        assertEquals(40.0, snapshot.gridLatitude)
        assertEquals(0.0, snapshot.temperature)
    }
    @Test fun absentNumbersAreNotZeroAndPartialDoesNotInventDays() {
        val dto = Gson().fromJson("""{"timezone":"Asia/Shanghai","utc_offset_seconds":28800,"current":{},"daily":{}}""", ForecastDto::class.java)
        val snapshot = mapper.snapshot(city, dto)
        assertNull(snapshot.temperature)
        assertNull(snapshot.code)
        assertTrue(snapshot.days.isEmpty())
        assertFalse(snapshot.complete)
    }
    @Test fun shortArraysKeepOriginalDateIndexAndNullDoesNotShiftValues() {
        val dto = valid()
        val snapshot = mapper.snapshot(city, dto.copy(daily = dto.daily!!.copy(
            temperature_2m_max = listOf(10.0, null, 12.0), weather_code = listOf(0, 999, 2))))
        assertNull(snapshot.days[0].high)
        assertEquals(12.0, snapshot.days[1].high)
        assertNull(snapshot.days[2].high)
        assertEquals("天气状况未知", weatherText(snapshot.days[0].code))
        assertFalse(snapshot.complete)
    }
    @Test fun duplicateAndInvalidDatesCannotBecomeComplete() {
        val dto = valid()
        val times = dto.daily!!.time!!.toMutableList()
        times[2] = times[1]
        times[3] = "invalid"
        val result = mapper.snapshot(city, dto.copy(daily = dto.daily!!.copy(time = times)))
        assertFalse(result.complete)
        assertFalse(result.days.any { it.date.toString() == times[1] })
        assertEquals(4, result.days.size)
    }
    @Test fun invalidUnitsAndInvertedTemperaturesAreUnavailable() {
        val dto = valid()
        val inverted = mapper.snapshot(city, dto.copy(daily = dto.daily!!.copy(temperature_2m_min = List(8) { 20.0 })))
        assertTrue(inverted.days.all { it.high == null && it.low == null })
        val wrongUnits = mapper.snapshot(city, dto.copy(current_units = CurrentUnits("°F"), daily_units = null))
        assertNull(wrongUnits.temperature)
        assertTrue(wrongUnits.days.all { it.high == null && it.low == null })
        assertFalse(wrongUnits.complete)
    }
    @Test fun missingTimezoneIsADataContractError() {
        assertThrows(WeatherFailure::class.java) { mapper.snapshot(city, valid().copy(timezone = null)) }
    }
    @Test fun cityFiltersDistinguishNoTargetFromDamagedIdentityAndDeduplicate() {
        val good = CityDto(1, "北京", 39.9, 116.4, "CN", "PPLC", "北京市", "北京市")
        val cities = mapper.cities(SearchDto(listOf(good, good, good.copy(id = 2, feature_code = "ISL"))))
        assertEquals(1, cities.size)
        assertEquals("北京 · 北京市", cities.first().label)
        assertTrue(mapper.cities(SearchDto(listOf(good.copy(country_code = "US")))).isEmpty())
        assertThrows(WeatherFailure::class.java) { mapper.cities(SearchDto(listOf(good.copy(latitude = Double.NaN)))) }
    }
    @Test fun capturedRealApiResponseMapsWithoutChangingItsDatesOrTemperatures() {
        val raw = javaClass.getResource("/beijing-forecast.json")!!.readText()
        val dto = Gson().fromJson(raw, ForecastDto::class.java)
        val capturedDate = java.time.LocalDate.parse(dto.daily!!.time!!.first())
        val capturedClock = Clock.fixed(capturedDate.atTime(12, 0).atZone(BEIJING).toInstant(), BEIJING)
        val snapshot = WeatherMapper(capturedClock).snapshot(city, dto)
        assertTrue(snapshot.complete)
        assertEquals(dto.current!!.temperature_2m, snapshot.temperature)
        assertEquals(dto.daily!!.temperature_2m_max!![1], snapshot.days.first().high)
        assertEquals(targetDates(capturedDate), snapshot.days.map { it.date })
    }
    @Test fun allDocumentedCodesHaveChineseLabels() {
        val known = listOf(0,1,2,3,45,48,51,53,55,56,57,61,63,65,66,67,71,73,75,77,80,81,82,85,86,95,96,99)
        assertTrue(known.all { knownCode(it) && !weatherText(it).contains("未知") })
        assertFalse(knownCode(null))
        assertFalse(knownCode(100))
    }
}
