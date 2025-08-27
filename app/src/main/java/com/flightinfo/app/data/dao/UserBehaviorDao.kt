package com.flightinfo.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.flightinfo.app.data.model.AirlineCount
import com.flightinfo.app.data.model.AirportCount
import com.flightinfo.app.data.model.RecommendationCache
import com.flightinfo.app.data.model.UserBehavior
import com.flightinfo.app.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow

@Dao
interface UserBehaviorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehavior(behavior: UserBehavior): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBehaviors(behaviors: List<UserBehavior>)

    @Query("SELECT * FROM user_behavior WHERE timestamp >= :startTime ORDER BY timestamp DESC")
    fun getRecentBehaviors(startTime: Long): Flow<List<UserBehavior>>

    @Query("SELECT * FROM user_behavior WHERE actionType = :actionType ORDER BY timestamp DESC")
    fun getBehaviorsByType(actionType: String): Flow<List<UserBehavior>>

    @Query("SELECT * FROM user_behavior WHERE departureAirport = :airport OR arrivalAirport = :airport ORDER BY timestamp DESC")
    fun getBehaviorsByAirport(airport: String): Flow<List<UserBehavior>>

    @Query("SELECT * FROM user_behavior WHERE airline = :airline ORDER BY timestamp DESC")
    fun getBehaviorsByAirline(airline: String): Flow<List<UserBehavior>>

    @Query("SELECT COUNT(*) FROM user_behavior WHERE actionType = :actionType AND timestamp >= :startTime")
    suspend fun getBehaviorCount(actionType: String, startTime: Long): Int

    @Query("SELECT departureAirport, COUNT(*) as count FROM user_behavior GROUP BY departureAirport ORDER BY count DESC LIMIT 10")
    suspend fun getTopDepartureAirports(): List<AirportCount>

    @Query("SELECT arrivalAirport, COUNT(*) as count FROM user_behavior GROUP BY arrivalAirport ORDER BY count DESC LIMIT 10")
    suspend fun getTopArrivalAirports(): List<AirportCount>

    @Query("SELECT airline, COUNT(*) as count FROM user_behavior GROUP BY airline ORDER BY count DESC LIMIT 10")
    suspend fun getTopAirlines(): List<AirlineCount>

    @Query("DELETE FROM user_behavior WHERE timestamp < :cutoffTime")
    suspend fun cleanupOldBehaviors(cutoffTime: Long)

    @Query("SELECT * FROM user_preferences WHERE userId = :userId")
    fun getUserPreferences(userId: String): Flow<UserPreferences?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPreferences(preferences: UserPreferences)

    @Update
    suspend fun updateUserPreferences(preferences: UserPreferences)

    @Query("SELECT * FROM recommendation_cache WHERE cacheKey = :key")
    suspend fun getRecommendationCache(key: String): RecommendationCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommendationCache(cache: RecommendationCache)

    @Query("DELETE FROM recommendation_cache WHERE timestamp < :cutoffTime")
    suspend fun cleanupExpiredCache(cutoffTime: Long)
}
