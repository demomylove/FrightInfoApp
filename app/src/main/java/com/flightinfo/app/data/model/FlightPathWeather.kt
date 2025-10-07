package com.flightinfo.app.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * 航路天气数据模型
 */
data class FlightPathWeather(
    @SerializedName("flight_id")
    val flightId: String,
    @SerializedName("route_segments")
    val routeSegments: List<RouteSegment>,
    @SerializedName("overall_risk")
    val overallRisk: WeatherRiskLevel,
    @SerializedName("recommendations")
    val recommendations: List<String>,
    @SerializedName("last_updated")
    val lastUpdated: Date,
)

/**
 * 航路段数据
 */
data class RouteSegment(
    @SerializedName("segment_id")
    val segmentId: String,
    @SerializedName("start_lat")
    val startLatitude: Double,
    @SerializedName("start_lon")
    val startLongitude: Double,
    @SerializedName("end_lat")
    val endLatitude: Double,
    @SerializedName("end_lon")
    val endLongitude: Double,
    @SerializedName("altitude_ft")
    val altitudeFeet: Int,
    @SerializedName("weather_conditions")
    val weatherConditions: List<WeatherCondition>,
    @SerializedName("turbulence_warnings")
    val turbulenceWarnings: List<TurbulenceWarning>,
)

/**
 * 天气状况
 */
data class WeatherCondition(
    @SerializedName("type")
    val type: WeatherType,
    @SerializedName("severity")
    val severity: WeatherSeverity,
    @SerializedName("description")
    val description: String,
    @SerializedName("altitude_range")
    val altitudeRange: AltitudeRange?,
    @SerializedName("probability")
    val probability: Int,
)

/**
 * 颠簸预警
 */
data class TurbulenceWarning(
    @SerializedName("warning_id")
    val warningId: String,
    @SerializedName("level")
    val level: TurbulenceLevel,
    @SerializedName("start_time")
    val startTime: Date,
    @SerializedName("end_time")
    val endTime: Date,
    @SerializedName("location")
    val location: GeoLocation,
    @SerializedName("altitude_range")
    val altitudeRange: AltitudeRange,
    @SerializedName("description")
    val description: String,
    @SerializedName("source")
    val source: DataSource,
    @SerializedName("confidence")
    val confidence: Int,
)

/**
 * 地理位置
 */
data class GeoLocation(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("region_name")
    val regionName: String?,
)

/**
 * 高度范围
 */
data class AltitudeRange(
    @SerializedName("min_feet")
    val minFeet: Int,
    @SerializedName("max_feet")
    val maxFeet: Int,
)

/**
 * 备降机场建议
 */
data class AlternateAirportRecommendation(
    @SerializedName("airport_code")
    val airportCode: String,
    @SerializedName("airport_name")
    val airportName: String,
    @SerializedName("distance_nm")
    val distanceNauticalMiles: Double,
    @SerializedName("weather_conditions")
    val weatherConditions: String,
    @SerializedName("runway_conditions")
    val runwayConditions: String,
    @SerializedName("recommendation_reason")
    val recommendationReason: String,
    @SerializedName("priority")
    val priority: Int,
)

/**
 * 最佳飞行时段建议
 */
data class OptimalFlightWindow(
    @SerializedName("start_time")
    val startTime: Date,
    @SerializedName("end_time")
    val endTime: Date,
    @SerializedName("weather_score")
    val weatherScore: Int,
    @SerializedName("turbulence_probability")
    val turbulenceProbability: Int,
    @SerializedName("delay_probability")
    val delayProbability: Int,
    @SerializedName("reasons")
    val reasons: List<String>,
)

/**
 * 天气风险等级
 */
enum class WeatherRiskLevel {
    @SerializedName("minimal")
    MINIMAL,

    @SerializedName("low")
    LOW,

    @SerializedName("moderate")
    MODERATE,

    @SerializedName("high")
    HIGH,

    @SerializedName("severe")
    SEVERE,
}

/**
 * 天气类型
 */
enum class WeatherType {
    @SerializedName("clear")
    CLEAR,

    @SerializedName("clouds")
    CLOUDS,

    @SerializedName("rain")
    RAIN,

    @SerializedName("thunderstorm")
    THUNDERSTORM,

    @SerializedName("snow")
    SNOW,

    @SerializedName("ice")
    ICE,

    @SerializedName("fog")
    FOG,

    @SerializedName("wind")
    WIND,

    @SerializedName("turbulence")
    TURBULENCE,

    @SerializedName("volcanic_ash")
    VOLCANIC_ASH,
}

/**
 * 天气严重程度
 */
enum class WeatherSeverity {
    @SerializedName("light")
    LIGHT,

    @SerializedName("moderate")
    MODERATE,

    @SerializedName("severe")
    SEVERE,

    @SerializedName("extreme")
    EXTREME,
}

/**
 * 颠簸等级
 */
enum class TurbulenceLevel {
    @SerializedName("none")
    NONE,

    @SerializedName("light")
    LIGHT,

    @SerializedName("moderate")
    MODERATE,

    @SerializedName("severe")
    SEVERE,

    @SerializedName("extreme")
    EXTREME,
}

/**
 * 数据源
 */
enum class DataSource {
    @SerializedName("noaa")
    NOAA,

    @SerializedName("nasa")
    NASA,

    @SerializedName("pilot_report")
    PILOT_REPORT,

    @SerializedName("satellite")
    SATELLITE,

    @SerializedName("radar")
    RADAR,

    @SerializedName("model_forecast")
    MODEL_FORECAST,
}

/**
 * 航路天气响应
 */
data class FlightPathWeatherResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: FlightPathWeather?,
    @SerializedName("alternate_airports")
    val alternateAirports: List<AlternateAirportRecommendation>,
    @SerializedName("optimal_windows")
    val optimalWindows: List<OptimalFlightWindow>,
    @SerializedName("message")
    val message: String?,
)
