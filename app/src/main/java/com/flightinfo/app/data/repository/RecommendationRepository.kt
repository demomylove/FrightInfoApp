package com.flightinfo.app.data.repository

import android.content.Context
import com.flightinfo.app.data.dao.UserBehaviorDao
import com.flightinfo.app.data.model.FlightInfo
import com.flightinfo.app.data.model.FlightRecommendation
import com.flightinfo.app.data.model.RecommendationAnalytics
import com.flightinfo.app.data.model.RecommendationType
import com.flightinfo.app.data.model.UserBehaviorStats
import com.flightinfo.app.data.model.UserPreferences
import com.flightinfo.app.utils.RecommendationEngine
import com.flightinfo.app.utils.Resource
import com.flightinfo.app.utils.UserBehaviorTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userBehaviorDao: UserBehaviorDao,
) {

    private val userBehaviorTracker by lazy {
        UserBehaviorTracker(context, userBehaviorDao)
    }

    private val recommendationEngine by lazy {
        RecommendationEngine(userBehaviorDao, userBehaviorTracker)
    }

    fun getRecommendations(): Flow<Resource<List<FlightRecommendation>>> = flow {
        try {
            emit(Resource.Loading())

            // 生成模拟航班数据用于演示
            val flights = generateMockFlights()

            // 生成推荐
            val recommendations = recommendationEngine.generateRecommendations(flights)

            emit(Resource.Success(recommendations))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "获取推荐失败"))
        }
    }

    private fun generateMockFlights(): List<FlightInfo> {
        return listOf(
            FlightInfo(
                flightNumber = "CA1234",
                airline = "中国国际航空",
                departureAirport = "PEK",
                arrivalAirport = "SHA",
                departureTime = "08:30:00",
                arrivalTime = "10:45:00",
                status = "准时",
                gate = null,
                terminal = null,
                delay = 0,
                aircraftType = null,
                carbonFootprint = null,
                price = 880.0,
                currency = "CNY",
            ),
            FlightInfo(
                flightNumber = "MU5678",
                airline = "中国东方航空",
                departureAirport = "SHA",
                arrivalAirport = "CAN",
                departureTime = "12:15:00",
                arrivalTime = "14:30:00",
                status = "延误",
                gate = null,
                terminal = null,
                delay = 0,
                aircraftType = null,
                carbonFootprint = null,
                price = 620.0,
                currency = "CNY",
            ),
            FlightInfo(
                flightNumber = "CZ9012",
                airline = "中国南方航空",
                departureAirport = "CAN",
                arrivalAirport = "PEK",
                departureTime = "16:45:00",
                arrivalTime = "19:00:00",
                status = "准时",
                gate = null,
                terminal = null,
                delay = 0,
                aircraftType = null,
                carbonFootprint = null,
                price = 750.0,
                currency = "CNY",
            ),
        )
    }

    fun getUserBehaviorStats(): Flow<Resource<UserBehaviorStats>> = flow {
        try {
            emit(Resource.Loading())
            val stats = userBehaviorTracker.getUserBehaviorStats()
            emit(Resource.Success(stats))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "获取用户统计失败"))
        }
    }

    fun getRecommendationAnalytics(): Flow<Resource<RecommendationAnalytics>> = flow {
        try {
            emit(Resource.Loading())
            val analytics = recommendationEngine.getRecommendationAnalytics()
            emit(Resource.Success(analytics))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "获取推荐分析失败"))
        }
    }

    fun trackUserAction(action: String, flightInfo: FlightInfo? = null) {
        when (action) {
            "search" -> flightInfo?.let {
                userBehaviorTracker.trackSearch(
                    it.flightNumber,
                    it.departureAirport,
                    it.arrivalAirport,
                    it.airline,
                )
            }
            "bookmark" -> flightInfo?.let { userBehaviorTracker.trackBookmark(it) }
            "track" -> flightInfo?.let { userBehaviorTracker.trackTrack(it) }
            "view" -> flightInfo?.let { userBehaviorTracker.trackView(it) }
            "book" -> flightInfo?.let { userBehaviorTracker.trackBook(it) }
        }
    }

    suspend fun trackRecommendationInteraction(
        recommendation: FlightRecommendation,
        interactionType: String,
    ) {
        recommendationEngine.trackRecommendationInteraction(recommendation, interactionType)
    }

    fun getUserPreferences(): Flow<Resource<UserPreferences>> = flow {
        try {
            emit(Resource.Loading())
            val preferences = userBehaviorDao.getUserPreferences("default_user")
            preferences.collect { prefs ->
                prefs?.let {
                    emit(Resource.Success(it))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "获取用户偏好失败"))
        }
    }

    fun updateUserPreferences(preferences: UserPreferences): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            userBehaviorDao.insertUserPreferences(preferences.copy(lastUpdated = System.currentTimeMillis()))
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "更新用户偏好失败"))
        }
    }

    fun clearOldData(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            userBehaviorTracker.cleanupOldData()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "清理旧数据失败"))
        }
    }

    // 生成个性化推荐理由
    fun generatePersonalizedReasons(recommendation: FlightRecommendation): List<String> {
        val reasons = mutableListOf<String>()

        when (recommendation.recommendationType) {
            RecommendationType.BASED_ON_HISTORY -> {
                reasons.add("基于您的旅行历史")
                if (recommendation.confidenceScore > 0.8) {
                    reasons.add("高度匹配您的偏好")
                }
            }
            RecommendationType.POPULAR_ROUTE -> {
                reasons.add("热门航线推荐")
                reasons.add("其他用户常选")
            }
            RecommendationType.PRICE_DROP -> {
                reasons.add("价格优惠")
                if (recommendation.flight.price != null) {
                    reasons.add("性价比高")
                }
            }
            RecommendationType.SIMILAR_USERS -> {
                reasons.add("相似用户喜欢")
                reasons.add("推荐指数高")
            }
            RecommendationType.TRENDING -> {
                reasons.add("当前热门")
                reasons.add("趋势推荐")
            }
            RecommendationType.PERSONALIZED -> {
                reasons.add("个性化推荐")
                reasons.add("专为您定制")
            }
        }

        // 基于置信度添加额外理由
        if (recommendation.confidenceScore > 0.9) {
            reasons.add("强烈推荐")
        } else if (recommendation.confidenceScore > 0.7) {
            reasons.add("推荐指数高")
        }

        return reasons
    }

    // 获取推荐类型的中文名称
    fun getRecommendationTypeName(type: RecommendationType): String {
        return when (type) {
            RecommendationType.BASED_ON_HISTORY -> "基于历史"
            RecommendationType.POPULAR_ROUTE -> "热门航线"
            RecommendationType.PRICE_DROP -> "价格优惠"
            RecommendationType.SIMILAR_USERS -> "相似用户"
            RecommendationType.TRENDING -> "趋势推荐"
            RecommendationType.PERSONALIZED -> "个性化推荐"
        }
    }
}
