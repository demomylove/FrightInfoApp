package com.flightinfo.app.utils

import com.flightinfo.app.data.dao.UserBehaviorDao
import com.flightinfo.app.data.model.FlightInfo
import com.flightinfo.app.data.model.FlightRecommendation
import com.flightinfo.app.data.model.RecommendationAnalytics
import com.flightinfo.app.data.model.RecommendationCache
import com.flightinfo.app.data.model.RecommendationType
import com.flightinfo.app.data.model.UserBehaviorStats
import com.flightinfo.app.data.model.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class RecommendationEngine(
    private val userBehaviorDao: UserBehaviorDao,
    private val userBehaviorTracker: UserBehaviorTracker,
) {

    suspend fun generateRecommendations(
        allFlights: List<FlightInfo>,
        maxRecommendations: Int = 10,
    ): List<FlightRecommendation> {
        val cacheKey = "recommendations_${System.currentTimeMillis() / (6 * 60 * 60 * 1000)}" // 6小时缓存

        // 检查缓存
        val cachedRecommendations = userBehaviorDao.getRecommendationCache(cacheKey)
        if (cachedRecommendations != null && System.currentTimeMillis() - cachedRecommendations.timestamp < cachedRecommendations.ttl) {
            return cachedRecommendations.recommendations.take(maxRecommendations)
        }

        // 获取用户偏好
        val userPreferences = userBehaviorDao.getUserPreferences("default_user").firstOrNull()
        val userStats = userBehaviorTracker.getUserBehaviorStats()

        // 生成不同类型的推荐
        val recommendations = mutableListOf<FlightRecommendation>()

        // 1. 基于历史行为的推荐
        recommendations.addAll(generateHistoryBasedRecommendations(allFlights, userPreferences, userStats))

        // 2. 热门航线推荐
        recommendations.addAll(generatePopularRouteRecommendations(allFlights, userStats))

        // 3. 价格推荐
        recommendations.addAll(generatePriceBasedRecommendations(allFlights, userPreferences))

        // 4. 航空公司偏好推荐
        recommendations.addAll(generateAirlineBasedRecommendations(allFlights, userPreferences))

        // 5. 个性化推荐
        recommendations.addAll(generatePersonalizedRecommendations(allFlights, userPreferences, userStats))

        // 去重并排序
        val uniqueRecommendations = recommendations
            .distinctBy { it.flight.flightNumber }
            .sortedByDescending { it.confidenceScore }
            .take(maxRecommendations)

        // 缓存结果
        userBehaviorDao.insertRecommendationCache(
            RecommendationCache(
                cacheKey = cacheKey,
                recommendations = uniqueRecommendations,
                timestamp = System.currentTimeMillis(),
                ttl = 6 * 60 * 60 * 1000, // 6小时
            ),
        )

        return uniqueRecommendations
    }

    private suspend fun generateHistoryBasedRecommendations(
        allFlights: List<FlightInfo>,
        userPreferences: UserPreferences?,
        userStats: UserBehaviorStats,
    ): List<FlightRecommendation> {
        val recommendations = mutableListOf<FlightRecommendation>()

        // 基于常用机场推荐
        userPreferences?.favoriteAirports?.forEach { airport ->
            val relatedFlights = allFlights.filter {
                it.departureAirport == airport || it.arrivalAirport == airport
            }

            relatedFlights.take(3).forEach { flight ->
                recommendations.add(
                    FlightRecommendation(
                        flight = flight,
                        recommendationType = RecommendationType.BASED_ON_HISTORY,
                        confidenceScore = calculateAirportConfidence(airport, userStats),
                        reasons = listOf("您经常使用机场 $airport", "基于您的旅行历史"),
                    ),
                )
            }
        }

        return recommendations
    }

    private fun generatePopularRouteRecommendations(
        allFlights: List<FlightInfo>,
        userStats: UserBehaviorStats,
    ): List<FlightRecommendation> {
        // 模拟热门航线数据
        val popularRoutes = listOf(
            "PEK-SHA", "SHA-CAN", "CAN-PEK", "PEK-SZX", "SZX-PEK",
            "SHA-CTU", "CTU-SHA", "PEK-CTU", "CTU-PEK", "CAN-SHA",
        )

        return popularRoutes.flatMap { route ->
            val (departure, arrival) = route.split("-")
            allFlights.filter {
                it.departureAirport == departure && it.arrivalAirport == arrival
            }.take(2).map { flight ->
                FlightRecommendation(
                    flight = flight,
                    recommendationType = RecommendationType.POPULAR_ROUTE,
                    confidenceScore = 0.7 + Random.nextDouble(0.2),
                    reasons = listOf("热门航线", "其他用户常选"),
                )
            }
        }
    }

    private fun generatePriceBasedRecommendations(
        allFlights: List<FlightInfo>,
        userPreferences: UserPreferences?,
    ): List<FlightRecommendation> {
        val recommendations = mutableListOf<FlightRecommendation>()

        // 基于价格偏好推荐
        userPreferences?.preferredPriceRange?.let { priceRange ->
            val priceMatchingFlights = allFlights.filter { flight ->
                flight.price?.let { price ->
                    price in priceRange.min..priceRange.max
                } ?: false
            }

            priceMatchingFlights.take(3).forEach { flight ->
                recommendations.add(
                    FlightRecommendation(
                        flight = flight,
                        recommendationType = RecommendationType.PRICE_DROP,
                        confidenceScore = 0.6 + Random.nextDouble(0.3),
                        reasons = listOf("符合您的价格偏好", "性价比高"),
                    ),
                )
            }
        }

        // 价格下降推荐（模拟）
        val discountedFlights = allFlights
            .filter { it.price != null }
            .shuffled()
            .take(2)
            .map { flight ->
                FlightRecommendation(
                    flight = flight.copy(price = flight.price!! * 0.8), // 模拟20%折扣
                    recommendationType = RecommendationType.PRICE_DROP,
                    confidenceScore = 0.8 + Random.nextDouble(0.1),
                    reasons = listOf("限时优惠", "价格下降20%"),
                )
            }

        recommendations.addAll(discountedFlights)
        return recommendations
    }

    private fun generateAirlineBasedRecommendations(
        allFlights: List<FlightInfo>,
        userPreferences: UserPreferences?,
    ): List<FlightRecommendation> {
        val recommendations = mutableListOf<FlightRecommendation>()

        userPreferences?.favoriteAirlines?.forEach { airline ->
            val airlineFlights = allFlights.filter { it.airline == airline }

            airlineFlights.take(2).forEach { flight ->
                recommendations.add(
                    FlightRecommendation(
                        flight = flight,
                        recommendationType = RecommendationType.PERSONALIZED,
                        confidenceScore = 0.65 + Random.nextDouble(0.25),
                        reasons = listOf("您偏好的航空公司 $airline", "优质服务"),
                    ),
                )
            }
        }

        return recommendations
    }

    private fun generatePersonalizedRecommendations(
        allFlights: List<FlightInfo>,
        userPreferences: UserPreferences?,
        userStats: UserBehaviorStats,
    ): List<FlightRecommendation> {
        val recommendations = mutableListOf<FlightRecommendation>()

        // 基于旅行频率的推荐
        userPreferences?.travelFrequency?.let { travelFrequency ->
            if (travelFrequency > 5) {
                // 高频旅行者推荐
                val frequentFlyerFlights = allFlights
                    .filter { it.airline in listOf("中国国际航空", "中国东方航空", "中国南方航空") }
                    .shuffled()
                    .take(2)

                frequentFlyerFlights.forEach { flight ->
                    recommendations.add(
                        FlightRecommendation(
                            flight = flight,
                            recommendationType = RecommendationType.PERSONALIZED,
                            confidenceScore = 0.75 + Random.nextDouble(0.15),
                            reasons = listOf("高频旅行者专享", "常旅客优惠"),
                        ),
                    )
                }
            }
        }

        // 基于最近活动的推荐
        if (userStats.recentActivityCount > 10) {
            val activeUserFlights = allFlights
                .filter { it.status == "准时" }
                .shuffled()
                .take(1)

            activeUserFlights.forEach { flight ->
                recommendations.add(
                    FlightRecommendation(
                        flight = flight,
                        recommendationType = RecommendationType.TRENDING,
                        confidenceScore = 0.7 + Random.nextDouble(0.2),
                        reasons = listOf("准点率高", "当前热门"),
                    ),
                )
            }
        }

        return recommendations
    }

    private fun calculateAirportConfidence(
        airport: String,
        userStats: UserBehaviorStats,
    ): Double {
        var confidence = 0.5

        // 如果是常用机场，增加置信度
        if (airport in userStats.topDepartureAirports || airport in userStats.topArrivalAirports) {
            confidence += 0.3
        }

        // 基于活跃度调整
        if (userStats.recentActivityCount > 5) {
            confidence += 0.1
        }

        return confidence.coerceAtMost(1.0)
    }

    suspend fun trackRecommendationInteraction(
        recommendation: FlightRecommendation,
        interactionType: String, // "click", "book", "ignore"
    ) {
        when (interactionType) {
            "click" -> userBehaviorTracker.trackRecommendationClick(recommendation)
            "book" -> userBehaviorTracker.trackBook(recommendation.flight)
            "ignore" -> {
                // 可以记录忽略行为，用于优化推荐算法
                // 目前暂时不实现
            }
        }
    }

    suspend fun getRecommendationAnalytics(): RecommendationAnalytics {
        val oneWeekAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
        val recentBehaviors = userBehaviorDao.getRecentBehaviors(oneWeekAgo).firstOrNull() ?: emptyList()

        val totalRecommendations = recentBehaviors.count { it.actionType == "recommendation_click" }
        val totalBooks = recentBehaviors.count { it.actionType == "book" }

        val clickThroughRate = if (totalRecommendations > 0) {
            totalRecommendations.toDouble() / (totalRecommendations + 10) // 假设总共展示的数量
        } else {
            0.0
        }

        val conversionRate = if (totalRecommendations > 0) {
            totalBooks.toDouble() / totalRecommendations
        } else {
            0.0
        }

        val recommendationTypes = recentBehaviors
            .filter { it.actionType == "recommendation_click" }
            .mapNotNull { it.metadata }
            .flatMap { metadata ->
                try {
                    val type = metadata.split("\"recommendation_type\":\"")[1].split("\"")[0]
                    listOf(RecommendationType.valueOf(type))
                } catch (e: Exception) {
                    emptyList<RecommendationType>()
                }
            }
            .groupingBy { it }
            .eachCount()

        return RecommendationAnalytics(
            totalRecommendations = totalRecommendations,
            clickThroughRate = clickThroughRate,
            conversionRate = conversionRate,
            averageConfidence = 0.75, // 模拟平均置信度
            topRecommendationTypes = recommendationTypes,
        )
    }
}
