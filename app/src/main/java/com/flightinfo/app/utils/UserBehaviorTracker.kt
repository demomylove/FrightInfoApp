package com.flightinfo.app.utils

import android.content.Context
import com.flightinfo.app.data.dao.UserBehaviorDao
import com.flightinfo.app.data.model.FlightInfo
import com.flightinfo.app.data.model.FlightRecommendation
import com.flightinfo.app.data.model.PriceRange
import com.flightinfo.app.data.model.UserBehavior
import com.flightinfo.app.data.model.UserBehaviorStats
import com.flightinfo.app.data.model.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class UserBehaviorTracker(
    private val context: Context,
    private val userBehaviorDao: UserBehaviorDao,
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun trackSearch(
        flightNumber: String?,
        departureAirport: String?,
        arrivalAirport: String?,
        airline: String?,
    ) {
        val behavior = UserBehavior(
            actionType = "search",
            flightNumber = flightNumber,
            departureAirport = departureAirport,
            arrivalAirport = arrivalAirport,
            airline = airline,
            priceRange = null,
            timestamp = System.currentTimeMillis(),
        )
        scope.launch {
            userBehaviorDao.insertBehavior(behavior)
            updateUserPreferences()
        }
    }

    fun trackBookmark(flightInfo: FlightInfo) {
        val behavior = UserBehavior(
            actionType = "bookmark",
            flightNumber = flightInfo.flightNumber,
            departureAirport = flightInfo.departureAirport,
            arrivalAirport = flightInfo.arrivalAirport,
            airline = flightInfo.airline,
            priceRange = flightInfo.price?.let { "${it - 100}-${it + 100}" },
            timestamp = System.currentTimeMillis(),
        )
        scope.launch {
            userBehaviorDao.insertBehavior(behavior)
            updateUserPreferences()
        }
    }

    fun trackTrack(flightInfo: FlightInfo) {
        val behavior = UserBehavior(
            actionType = "track",
            flightNumber = flightInfo.flightNumber,
            departureAirport = flightInfo.departureAirport,
            arrivalAirport = flightInfo.arrivalAirport,
            airline = flightInfo.airline,
            priceRange = flightInfo.price?.let { "${it - 100}-${it + 100}" },
            timestamp = System.currentTimeMillis(),
        )
        scope.launch {
            userBehaviorDao.insertBehavior(behavior)
            updateUserPreferences()
        }
    }

    fun trackView(flightInfo: FlightInfo) {
        val behavior = UserBehavior(
            actionType = "view",
            flightNumber = flightInfo.flightNumber,
            departureAirport = flightInfo.departureAirport,
            arrivalAirport = flightInfo.arrivalAirport,
            airline = flightInfo.airline,
            priceRange = flightInfo.price?.let { "${it - 100}-${it + 100}" },
            timestamp = System.currentTimeMillis(),
        )
        scope.launch {
            userBehaviorDao.insertBehavior(behavior)
        }
    }

    fun trackBook(flightInfo: FlightInfo) {
        val behavior = UserBehavior(
            actionType = "book",
            flightNumber = flightInfo.flightNumber,
            departureAirport = flightInfo.departureAirport,
            arrivalAirport = flightInfo.arrivalAirport,
            airline = flightInfo.airline,
            priceRange = flightInfo.price?.let { "${it - 100}-${it + 100}" },
            timestamp = System.currentTimeMillis(),
        )
        scope.launch {
            userBehaviorDao.insertBehavior(behavior)
            updateUserPreferences()
        }
    }

    fun trackRecommendationClick(recommendation: FlightRecommendation) {
        val behavior = UserBehavior(
            actionType = "recommendation_click",
            flightNumber = recommendation.flight.flightNumber,
            departureAirport = recommendation.flight.departureAirport,
            arrivalAirport = recommendation.flight.arrivalAirport,
            airline = recommendation.flight.airline,
            priceRange = recommendation.flight.price?.let { price -> "${price - 100}-${price + 100}" },
            timestamp = System.currentTimeMillis(),
            metadata = "{\"recommendation_type\":\"${recommendation.recommendationType.name}\",\"confidence_score\":${recommendation.confidenceScore}}",
        )
        scope.launch {
            userBehaviorDao.insertBehavior(behavior)
        }
    }

    private suspend fun updateUserPreferences() {
        try {
            val currentPrefs = userBehaviorDao.getUserPreferences("default_user").firstOrNull()
            val oneMonthAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)

            // 获取用户行为统计
            val topDepartureAirports = userBehaviorDao.getTopDepartureAirports()
            val topArrivalAirports = userBehaviorDao.getTopArrivalAirports()
            val topAirlines = userBehaviorDao.getTopAirlines()
            val searchCount = userBehaviorDao.getBehaviorCount("search", oneMonthAgo)
            val bookCount = userBehaviorDao.getBehaviorCount("book", oneMonthAgo)

            // 合并常用机场
            val allAirports = (topDepartureAirports + topArrivalAirports)
                .flatMap {
                    listOfNotNull(
                        it.departureAirport?.let { airport -> airport to it.count },
                        it.arrivalAirport?.let { airport -> airport to it.count },
                    )
                }
                .groupBy { it.first }
                .map { (airport, counts) -> airport to counts.sumOf { it.second } }
                .sortedByDescending { it.second }
                .take(10)
                .map { it.first }

            // 获取价格范围偏好
            val recentSearches = userBehaviorDao.getRecentBehaviors(oneMonthAgo).firstOrNull() ?: emptyList()
            val prices = recentSearches
                .filter { it.priceRange != null }
                .mapNotNull { it.priceRange }
                .flatMap {
                    val parts = it.split("-")
                    if (parts.size == 2) {
                        try {
                            listOf(parts[0].toDouble(), parts[1].toDouble())
                        } catch (e: Exception) {
                            emptyList()
                        }
                    } else {
                        emptyList()
                    }
                }

            val priceRange = if (prices.isNotEmpty()) {
                val min = prices.minOrNull() ?: 0.0
                val max = prices.maxOrNull() ?: 1000.0
                PriceRange(min, max)
            } else {
                currentPrefs?.preferredPriceRange
            }

            val updatedPrefs = UserPreferences(
                userId = "default_user",
                favoriteAirlines = topAirlines.map { it.airline },
                favoriteAirports = allAirports,
                preferredPriceRange = priceRange,
                preferredAirlines = topAirlines.map { it.airline },
                travelFrequency = searchCount.coerceAtLeast(1),
                lastUpdated = System.currentTimeMillis(),
            )

            userBehaviorDao.insertUserPreferences(updatedPrefs)
        } catch (e: Exception) {
            // 记录错误但不影响主要功能
            e.printStackTrace()
        }
    }

    suspend fun getUserBehaviorStats(): UserBehaviorStats {
        val oneMonthAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
        val oneWeekAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)

        return withContext(Dispatchers.IO) {
            val topDepartureAirports = userBehaviorDao.getTopDepartureAirports()
            val topArrivalAirports = userBehaviorDao.getTopArrivalAirports()

            UserBehaviorStats(
                totalSearchesLastMonth = userBehaviorDao.getBehaviorCount("search", oneMonthAgo),
                totalBookmarksLastMonth = userBehaviorDao.getBehaviorCount("bookmark", oneMonthAgo),
                totalTracksLastMonth = userBehaviorDao.getBehaviorCount("track", oneMonthAgo),
                totalBooksLastMonth = userBehaviorDao.getBehaviorCount("book", oneMonthAgo),
                topDepartureAirports = topDepartureAirports.mapNotNull { it.departureAirport },
                topArrivalAirports = topArrivalAirports.mapNotNull { it.arrivalAirport },
                topAirlines = userBehaviorDao.getTopAirlines().map { it.airline },
                recentActivityCount = userBehaviorDao.getBehaviorCount("view", oneWeekAgo),
            )
        }
    }

    fun cleanupOldData() {
        scope.launch {
            val oneMonthAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
            userBehaviorDao.cleanupOldBehaviors(oneMonthAgo)
            userBehaviorDao.cleanupExpiredCache(System.currentTimeMillis())
        }
    }
}

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
