package com.flightinfo.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "user_behavior")
data class UserBehavior(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val actionType: String,
    val flightNumber: String?,
    val departureAirport: String?,
    val arrivalAirport: String?,
    val airline: String?,
    val priceRange: String?,
    val timestamp: Long,
    val metadata: String? = null,
)

@Entity(tableName = "user_preferences")
data class UserPreferences(
    @PrimaryKey
    val userId: String = "default_user",
    val favoriteAirlines: List<String> = emptyList(),
    val favoriteAirports: List<String> = emptyList(),
    val preferredPriceRange: PriceRange? = null,
    val preferredAirlines: List<String> = emptyList(),
    val travelFrequency: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis(),
)

data class PriceRange(
    val min: Double,
    val max: Double,
    val currency: String = "USD",
)

@Entity(tableName = "recommendation_cache")
data class RecommendationCache(
    @PrimaryKey
    val cacheKey: String,
    val recommendations: List<FlightRecommendation>,
    val timestamp: Long,
    val ttl: Long = 24 * 60 * 60 * 1000,
)

data class FlightRecommendation(
    val flight: FlightInfo,
    val recommendationType: RecommendationType,
    val confidenceScore: Double,
    val reasons: List<String>,
    val personalizedScore: Double = 0.0,
)

enum class RecommendationType {
    BASED_ON_HISTORY, // 基于历史行为
    POPULAR_ROUTE, // 热门航线
    PRICE_DROP, // 价格下降
    SIMILAR_USERS, // 相似用户喜欢
    TRENDING, // 趋势推荐
    PERSONALIZED, // 个性化推荐
}

data class RecommendationAnalytics(
    val totalRecommendations: Int = 0,
    val clickThroughRate: Double = 0.0,
    val conversionRate: Double = 0.0,
    val averageConfidence: Double = 0.0,
    val topRecommendationTypes: Map<RecommendationType, Int> = emptyMap(),
)

data class AirportCount(
    val departureAirport: String?,
    val arrivalAirport: String?,
    val count: Int,
)

data class AirlineCount(
    val airline: String,
    val count: Int,
)

data class UserBehaviorStats(
    val totalSearchesLastMonth: Int = 0,
    val totalBookmarksLastMonth: Int = 0,
    val totalTracksLastMonth: Int = 0,
    val totalBooksLastMonth: Int = 0,
    val topDepartureAirports: List<String> = emptyList(),
    val topArrivalAirports: List<String> = emptyList(),
    val topAirlines: List<String> = emptyList(),
    val recentActivityCount: Int = 0,
)

class RecommendationConverters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromPriceRange(priceRange: PriceRange?): String {
        return Gson().toJson(priceRange)
    }

    @TypeConverter
    fun toPriceRange(priceRangeString: String): PriceRange? {
        return Gson().fromJson(priceRangeString, PriceRange::class.java)
    }

    @TypeConverter
    fun fromRecommendationList(recommendations: List<FlightRecommendation>): String {
        return Gson().toJson(recommendations)
    }

    @TypeConverter
    fun toRecommendationList(recommendationsString: String): List<FlightRecommendation> {
        val listType = object : TypeToken<List<FlightRecommendation>>() {}.type
        return Gson().fromJson(recommendationsString, listType)
    }

    @TypeConverter
    fun fromRecommendationType(type: RecommendationType): String {
        return type.name
    }

    @TypeConverter
    fun toRecommendationType(typeString: String): RecommendationType {
        return RecommendationType.valueOf(typeString)
    }
}
