package com.flightinfo.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flightinfo.app.data.model.HistoricalFlight
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoricalFlightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historicalFlight: HistoricalFlight)

    @Query("SELECT * FROM historical_flights ORDER BY accessedTimestamp DESC")
    fun getHistoricalFlights(): Flow<List<HistoricalFlight>>

    @Query("DELETE FROM historical_flights")
    suspend fun clearHistory()
}
