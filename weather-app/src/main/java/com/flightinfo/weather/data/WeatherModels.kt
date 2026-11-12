package com.flightinfo.weather.data

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

val BEIJING: ZoneId = ZoneId.of("Asia/Shanghai")
fun today(clock: Clock): LocalDate = LocalDate.now(clock.withZone(BEIJING))
fun targetDates(date: LocalDate): List<LocalDate> = (1L..7L).map(date::plusDays)

data class City(val id: Long, val name: String, val regions: List<String>, val latitude: Double, val longitude: Double, val timezone: String?) {
    val label: String get() = (listOf(name) + regions).distinct().joinToString(" · ") +
        if (regions.isEmpty()) " · $latitude, $longitude" else ""
}
data class DayWeather(val date: LocalDate, val code: Int?, val high: Double?, val low: Double?) {
    val complete: Boolean get() = knownCode(code) && high != null && low != null
}
data class WeatherSnapshot(
    val city: City,
    val receivedAt: Instant,
    val referenceDate: LocalDate,
    val dataTime: LocalDateTime?,
    val temperature: Double?,
    val code: Int?,
    val days: List<DayWeather>,
    val gridLatitude: Double?,
    val gridLongitude: Double?,
    val dateIssues: Boolean = false,
) {
    val complete: Boolean get() = dataTime != null && temperature != null && knownCode(code) &&
        !dateIssues && days.map { it.date } == targetDates(referenceDate) && days.all { it.complete }
    fun forDate(date: LocalDate) = copy(referenceDate = date, days = days.filter { it.date in targetDates(date) })
}

private val codes = mapOf(
    0 to "晴", 1 to "大部晴朗", 2 to "多云", 3 to "阴", 45 to "雾", 48 to "雾凇",
    51 to "小毛毛雨", 53 to "中等毛毛雨", 55 to "强毛毛雨", 56 to "弱冻毛毛雨", 57 to "强冻毛毛雨",
    61 to "小雨", 63 to "中雨", 65 to "大雨", 66 to "小冻雨", 67 to "强冻雨",
    71 to "小雪", 73 to "中雪", 75 to "大雪", 77 to "雪粒", 80 to "小阵雨", 81 to "中等阵雨",
    82 to "强阵雨", 85 to "小阵雪", 86 to "强阵雪", 95 to "雷雨", 96 to "雷雨伴小冰雹", 99 to "雷雨伴强冰雹",
)
fun knownCode(code: Int?) = code in codes
fun weatherText(code: Int?) = if (code == null) "天气状况暂无数据" else codes[code] ?: "天气状况未知"

class WeatherFailure(val kind: Kind) : Exception(kind.name) {
    enum class Kind { NETWORK, SERVICE, RATE_LIMIT, FORMAT }
    val userMessage: String get() = when (kind) {
        Kind.NETWORK -> "网络连接失败，请检查网络后重试"
        Kind.SERVICE -> "天气服务暂不可用，请稍后重试"
        Kind.RATE_LIMIT -> "请求过于频繁，请稍后重试"
        Kind.FORMAT -> "天气数据格式异常，请重试"
    }
}
